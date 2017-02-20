/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.javascript.jscomp;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.LimitInputStream;

import sketch4j.generator.SketchRequest;
import sketch4j.generator.statement.AssignmentCandidate;
import sketch4j.generator.statement.SymbolicCandidate;

/**
 * CommandLineRunner translates flags into Java API calls on the Compiler.
 *
 * This class may be extended and used to create other Java classes that behave
 * the same as running the Compiler from the command line. If you want to run
 * the compiler in-process in Java, you should look at this class for hints on
 * what API calls to make, but you should not use this class directly.
 *
 * Example:
 * 
 * <pre>
 * class MyCommandLineRunner extends CommandLineRunner {
 *   MyCommandLineRunner(String[] args) throws CmdLineException {
 *     super(args);
 *   }
 *
 *   {@code @Override} protected CompilerOptions createOptions() {
 *     CompilerOptions options = super.createOptions();
 *     addMyCrazyCompilerPassThatOutputsAnExtraFile(options);
 *     return options;
 *   }
 *
 *   public static void main(String[] args) {
 *     try {
 *       (new MyCommandLineRunner(args)).run();
 *     } catch (CmdLineException e) {
 *       System.exit(-1);
 *     }
 *   }
 * }
 * </pre>
 * 
 * @author bolinfest@google.com (Michael Bolin)
 */
public class CommandLineRunner extends AbstractCommandLineRunner<Compiler, CompilerOptions> {

    private static class Flags {
        @Option(name = "--print_tree", handler = BooleanOptionHandler.class, usage = "Prints out the parse tree and exits")
            private boolean print_tree = false;

        @Option(name = "--compute_phase_ordering", handler = BooleanOptionHandler.class, usage = "Runs the compile job many times, then prints out the "
                + "best phase ordering from this run")
            private boolean compute_phase_ordering = false;

        @Option(name = "--print_ast", handler = BooleanOptionHandler.class, usage = "Prints a dot file describing the internal abstract syntax"
                + " tree and exits")
            private boolean print_ast = false;

        @Option(name = "--print_pass_graph", usage = "Prints a dot file describing the passes that will get run"
                + " and exits")
            private boolean print_pass_graph = false;

        @Option(name = "--jscomp_dev_mode", usage = "Turns on extra sanity checks", aliases = { "--dev_mode" })
            private CompilerOptions.DevMode jscomp_dev_mode = CompilerOptions.DevMode.OFF;

        // TODO(nicksantos): Make the next 2 flags package-private.
        @Option(name = "--logging_level", usage = "The logging level (standard java.util.logging.Level"
                + " values) for Compiler progress. Does not control errors or"
                + " warnings for the JavaScript code under compilation")
            private String logging_level = Level.WARNING.getName();

        @Option(name = "--externs", usage = "The file containing javascript externs. You may specify" + " multiple")
            private List<String> externs = Lists.newArrayList();

        @Option(name = "--js", usage = "The javascript filename. You may specify multiple")
            private List<String> js = Lists.newArrayList();

        @Option(name = "--js_output_file", usage = "Primary output filename. If not specified, output is "
                + "written to stdout")
            private String js_output_file = "";

        @Option(name = "--module", usage = "A javascript module specification. The format is "
                + "<name>:<num-js-files>[:[<dep>,...][:]]]. Module names must be "
                + "unique. Each dep is the name of a module that this module "
                + "depends on. Modules must be listed in dependency order, and js "
                + "source files must be listed in the corresponding order. Where "
                + "--module flags occur in relation to --js flags is unimportant")
            private List<String> module = Lists.newArrayList();

        @Option(name = "--variable_map_input_file", usage = "File containing the serialized version of the variable "
                + "renaming map produced by a previous compilation")
            private String variable_map_input_file = "";

        @Option(name = "--property_map_input_file", usage = "File containing the serialized version of the property "
                + "renaming map produced by a previous compilation")
            private String property_map_input_file = "";

        @Option(name = "--variable_map_output_file", usage = "File where the serialized version of the variable "
                + "renaming map produced should be saved")
            private String variable_map_output_file = "";

        @Option(name = "--create_name_map_files", handler = BooleanOptionHandler.class, usage = "If true, variable renaming and property renaming map "
                + "files will be produced as {binary name}_vars_map.out and "
                + "{binary name}_props_map.out. Note that this flag cannot be used "
                + "in conjunction with either variable_map_output_file or " + "property_map_output_file")
            private boolean create_name_map_files = false;

        @Option(name = "--property_map_output_file", usage = "File where the serialized version of the property "
                + "renaming map produced should be saved")
            private String property_map_output_file = "";

        @Option(name = "--third_party", handler = BooleanOptionHandler.class, usage = "Check source validity but do not enforce Closure style "
                + "rules and conventions")
            private boolean third_party = false;

        @Option(name = "--summary_detail_level", usage = "Controls how detailed the compilation summary is. Values:"
                + " 0 (never print summary), 1 (print summary only if there are "
                + "errors or warnings), 2 (print summary if type checking is on, "
                + "see --check_types), 3 (always print summary). The default level " + "is 1")
            private int summary_detail_level = 1;

        @Option(name = "--output_wrapper", usage = "Interpolate output into this string at the place denoted"
                + " by the marker token %output%. See --output_wrapper_marker")
            private String output_wrapper = "";

        @Option(name = "--output_wrapper_marker", usage = "Use this token as output marker in the value of"
                + " --output_wrapper")
            private String output_wrapper_marker = "%output%";

        @Option(name = "--module_wrapper", usage = "An output wrapper for a javascript module (optional). "
                + "The format is <name>:<wrapper>. The module name must correspond "
                + "with a module specified using --module. The wrapper must " + "contain %s as the code placeholder")
            private List<String> module_wrapper = Lists.newArrayList();

        @Option(name = "--module_output_path_prefix", usage = "Prefix for filenames of compiled js modules. "
                + "<module-name>.js will be appended to this prefix. Directories "
                + "will be created as needed. Use with --module")
            private String module_output_path_prefix = "./";

        @Option(name = "--create_source_map", usage = "If specified, a source map file mapping the generated "
                + "source files back to the original source file will be "
                + "output to the specified path. The %outname% placeholder will "
                + "expand to the name of the output file that the source map " + "corresponds to.")
            private String create_source_map = "";

        @Option(name = "--jscomp_error", usage = "Make the named class of warnings an error. Options:"
                + DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES)
            private List<String> jscomp_error = Lists.newArrayList();

        @Option(name = "--jscomp_warning", usage = "Make the named class of warnings a normal warning. " + "Options:"
                + DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES)
            private List<String> jscomp_warning = Lists.newArrayList();

        @Option(name = "--jscomp_off", usage = "Turn off the named class of warnings. Options:"
                + DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES)
            private List<String> jscomp_off = Lists.newArrayList();

        @Option(name = "--define", aliases = { "--D",
            "-D" }, usage = "Override the value of a variable annotated @define. "
            + "The format is <name>[=<val>], where <name> is the name of a @define "
            + "variable and <val> is a boolean, number, or a single-quoted string "
            + "that contains no single quotes. If [=<val>] is omitted, " + "the variable is marked true")
            private List<String> define = Lists.newArrayList();

        @Option(name = "--charset", usage = "Input charset for all files.")
            private String charset = "";

        @Option(name = "--compilation_level", usage = "Specifies the compilation level to use. Options: "
                + "WHITESPACE_ONLY, SIMPLE_OPTIMIZATIONS, ADVANCED_OPTIMIZATIONS")
            private CompilationLevel compilation_level = CompilationLevel.SIMPLE_OPTIMIZATIONS;

        @Option(name = "--warning_level", usage = "Specifies the warning level to use. Options: "
                + "QUIET, DEFAULT, VERBOSE")
            private WarningLevel warning_level = WarningLevel.DEFAULT;

        @Option(name = "--use_only_custom_externs", handler = BooleanOptionHandler.class, usage = "Specifies whether the default externs should be excluded")
            private boolean use_only_custom_externs = false;

        @Option(name = "--debug", handler = BooleanOptionHandler.class, usage = "Enable debugging options")
            private boolean debug = false;

        @Option(name = "--formatting", usage = "Specifies which formatting options, if any, should be "
                + "applied to the output JS. Options: " + "PRETTY_PRINT, PRINT_INPUT_DELIMITER")
            private List<FormattingOption> formatting = Lists.newArrayList();

        @Option(name = "--process_closure_primitives", handler = BooleanOptionHandler.class, usage = "Processes built-ins from the Closure library, such as "
                + "goog.require(), goog.provide(), and goog.exportSymbol()")
            private boolean process_closure_primitives = true;

        // Our own option parser to be backwards-compatible.
        // It needs to be public because of the crazy reflection that args4j
        // does.
        public static class BooleanOptionHandler extends OptionHandler<Boolean> {
            private static final Set<String> TRUES = Sets.newHashSet("true", "on", "yes", "1");
            private static final Set<String> FALSES = Sets.newHashSet("false", "off", "no", "0");

            public BooleanOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Boolean> setter) {
                super(parser, option, setter);
            }

            @Override
            public int parseArguments(Parameters params) throws CmdLineException {
                String param = params.getParameter(0);
                if (param == null) {
                    setter.addValue(true);
                    return 0;
                } else {
                    String lowerParam = param.toLowerCase();
                    if (TRUES.contains(lowerParam)) {
                        setter.addValue(true);
                    } else if (FALSES.contains(lowerParam)) {
                        setter.addValue(false);
                    } else {
                        throw new CmdLineException(owner, "Illegal boolean value: " + lowerParam);
                    }
                    return 1;
                }
            }

            @Override
            public String getDefaultMetaVariable() {
                return null;
            }
        }
    }

    /**
     * Set of options that can be used with the --formatting flag.
     */
    private static enum FormattingOption {
        PRETTY_PRINT, PRINT_INPUT_DELIMITER,;

        private void applyToOptions(CompilerOptions options) {
            switch (this) {
                case PRETTY_PRINT:
                    options.prettyPrint = true;
                    break;
                case PRINT_INPUT_DELIMITER:
                    options.printInputDelimiter = true;
                    break;
                default:
                    throw new RuntimeException("Unknown formatting option: " + this);
            }
        }
    }

    private final Flags flags = new Flags();

    /**
     * Create a new command-line runner. You should only need to call the
     * constructor if you're extending this class. Otherwise, the main method
     * should instantiate it.
     */
    protected CommandLineRunner(String[] args) throws CmdLineException {
        super();
        initConfigFromFlags(args, System.err);
    }

    protected CommandLineRunner(String[] args, PrintStream out, PrintStream err) throws CmdLineException {
        super(out, err);
        initConfigFromFlags(args, err);
    }

    private void initConfigFromFlags(String[] args, PrintStream err) throws CmdLineException {
        // Args4j has a different format that the old command-line parser.
        // So we use some voodoo to get the args into the format that args4j
        // expects.
        Pattern argPattern = Pattern.compile("(--[a-zA-Z_]+)=(.*)");
        Pattern quotesPattern = Pattern.compile("^['\"](.*)['\"]$");
        List<String> processedArgs = Lists.newArrayList();
        for (String arg : args) {
            Matcher matcher = argPattern.matcher(arg);
            if (matcher.matches()) {
                processedArgs.add(matcher.group(1));

                String value = matcher.group(2);
                Matcher quotesMatcher = quotesPattern.matcher(value);
                if (quotesMatcher.matches()) {
                    processedArgs.add(quotesMatcher.group(1));
                } else {
                    processedArgs.add(value);
                }
            } else {
                processedArgs.add(arg);
            }
        }

        CmdLineParser parser = new CmdLineParser(flags);
        try {
            parser.parseArgument(processedArgs.toArray(new String[] {}));
        } catch (CmdLineException e) {
            err.println(e.getMessage());
            parser.printUsage(err);
            throw e;
        }
        getCommandLineConfig().setPrintTree(flags.print_tree).setComputePhaseOrdering(flags.compute_phase_ordering)
            .setPrintAst(flags.print_ast).setPrintPassGraph(flags.print_pass_graph)
            .setJscompDevMode(flags.jscomp_dev_mode).setLoggingLevel(flags.logging_level).setExterns(flags.externs)
            .setJs(flags.js).setJsOutputFile(flags.js_output_file).setModule(flags.module)
            .setVariableMapInputFile(flags.variable_map_input_file)
            .setPropertyMapInputFile(flags.property_map_input_file)
            .setVariableMapOutputFile(flags.variable_map_output_file)
            .setCreateNameMapFiles(flags.create_name_map_files)
            .setPropertyMapOutputFile(flags.property_map_output_file).setThirdParty(flags.third_party)
            .setSummaryDetailLevel(flags.summary_detail_level).setOutputWrapper(flags.output_wrapper)
            .setOutputWrapperMarker(flags.output_wrapper_marker).setModuleWrapper(flags.module_wrapper)
            .setModuleOutputPathPrefix(flags.module_output_path_prefix).setCreateSourceMap(flags.create_source_map)
            .setJscompError(flags.jscomp_error).setJscompWarning(flags.jscomp_warning)
            .setJscompOff(flags.jscomp_off).setDefine(flags.define).setCharset(flags.charset);
    }

    @Override
    protected CompilerOptions createOptions() {
        CompilerOptions options = new CompilerOptions();
        options.setCodingConvention(new ClosureCodingConvention());
        CompilationLevel level = flags.compilation_level;
        level.setOptionsForCompilationLevel(options);
        if (flags.debug) {
            level.setDebugOptionsForCompilationLevel(options);
        }

        WarningLevel wLevel = flags.warning_level;
        wLevel.setOptionsForWarningLevel(options);
        for (FormattingOption formattingOption : flags.formatting) {
            formattingOption.applyToOptions(options);
        }
        /** This is manually crossed out because Sketch4J focuses on synthesis and cannot handle repair deletion*/
        // if (flags.process_closure_primitives) {
        // options.closurePass = true;
        // }
        {
            _options_ = options;
        }
        /** Added by Sketch4J */
        _BLOCK_(SketchRequest.queryBlock(boolean.class, new String[] { "flags", "options", "level", "wLevel" },
                    new Object[] { flags, options, level, wLevel }, 1, 1, 0));
        //Manual added: Expected solution
        //		options.closurePass = flags.process_closure_primitives;
        initOptionsFromFlags(_options_);
        return _options_;
    }

    /** Added by Sketch4J */
    private void _BLOCK_(List<SymbolicCandidate> cand) {

        for (int i = 0; i < cand.size(); i++) {
            switch (cand.get(i).getType()) {
                case ASSIGNMENT:
                    assignment((AssignmentCandidate) cand.get(i), (i == 0) ? null : (AssignmentCandidate) cand.get(i - 1),
                            4);
                    break;
                default:
                    break;
            }

        }
    }

    /** Added by Sketch4J ***/
    private void assignment(AssignmentCandidate assign, AssignmentCandidate prev, int varLen) {
        int lid = assign.getJPFLHS();
        int rid = assign.getJPFRHS();
        if (SketchRequest.notValid(Arrays.asList(assign, prev))) {
            SketchRequest.backtrack();
        }
        boolean rhs = getRHS(rid);
        switch (lid) {
            case  0  :  flags.print_tree  = rhs; break;
            case  1  :  flags.compute_phase_ordering  = rhs; break;
            case  2  :  flags.print_ast = rhs; break;
            case  3  :  flags.print_pass_graph  = rhs; break;
            case  4  :  flags.create_name_map_files  = rhs; break;
            case  5  :  flags.third_party  = rhs; break;
            case   6 :  flags.use_only_custom_externs = rhs; break;
            case  7  :  flags.debug  = rhs; break;
            case  8  :  flags.process_closure_primitives  = rhs; break;
            case 10   :    _options_.ideMode = rhs; break;
            case 11   :    _options_.checkSymbols = rhs; break;
            case  12  :     _options_.checkDuplicateMessages = rhs; break;
            case  13  :     _options_.allowLegacyJsMessages = rhs; break;
            case 14:  _options_.printInputDelimiter = rhs; break;
            case 15   :     _options_.strictMessageReplacement = rhs; break;
            case  16  :     _options_.checkSuspiciousCode = rhs; break;
            case  17  :    _options_.checkControlStructures = rhs; break;
            case  18  :     _options_.checkUnusedPropertiesEarly = rhs; break;
            case  19  :    _options_.checkTypes = rhs; break;
            case  20  :    _options_.tightenTypes = rhs; break;
            case  21  :     _options_.inferTypesInGlobalScope = rhs; break;
            case  22  :     _options_.checkTypedPropertyCalls = rhs; break;
            case  23  :     _options_.checkEs5Strict = rhs; break;
            case  24  :     _options_.checkCaja = rhs; break;
            case   25 :    _options_.foldConstants = rhs; break;
            case  26  :     _options_.removeConstantExpressions = rhs; break;
            case  27  :     _options_.deadAssignmentElimination = rhs; break;
            case  28  :     _options_.inlineConstantVars = rhs; break;
            case  29  :     _options_.inlineFunctions = rhs; break;
            case  30  :     _options_.decomposeExpressions = rhs; break;
            case  31  :    _options_.inlineAnonymousFunctionExpressions = rhs; break;
            case  32  :    _options_.inlineLocalFunctions = rhs; break;
            case  33  :     _options_.crossModuleCodeMotion = rhs; break;
            case  34  :     _options_.coalesceVariableNames = rhs; break;
            case  35  :     _options_.crossModuleMethodMotion = rhs; break;
            case  36  :     _options_.inlineGetters = rhs; break;
            case  37  :     _options_.inlineVariables = rhs; break;
            case  38  :     _options_.inlineLocalVariables = rhs; break;
            case  39  :     _options_.flowSensitiveInlineVariables = rhs; break;   
            case  40  :     _options_.smartNameRemoval = rhs; break;
            case  41  :     _options_.removeDeadCode = rhs; break;
            case  42  :     _options_.extractPrototypeMemberDeclarations = rhs; break;
            case  43  :     _options_.removeEmptyFunctions = rhs; break;
            case  44  :     _options_.removeUnusedPrototypeProperties = rhs; break;
            case  45  :     _options_.removeUnusedPrototypePropertiesInExterns = rhs; break;
            case  46  :     _options_.removeUnusedVars = rhs; break;
            case 47   :     _options_.removeUnusedVarsInGlobalScope = rhs; break;
            case 48   :     _options_.aliasExternals = rhs; break;
            case  49  :    _options_.collapseVariableDeclarations = rhs; break;
            case 50   :     _options_.collapseAnonymousFunctions = rhs; break; 
            case  51  :   _options_.aliasAllStrings = rhs; break;
            case  52  :     _options_.convertToDottedProperties = rhs; break;
            case 53   :     _options_.rewriteFunctionExpressions = rhs; break;
            case  54  :     _options_.optimizeParameters = rhs; break;
            case  55  :     _options_.optimizeArgumentsArray = rhs; break;
            case  56  :     _options_.labelRenaming = rhs; break; 
            case  57  :     _options_.reserveRawExports = rhs; break;
            case  58  :     _options_.generatePseudoNames = rhs; break; 
            case 59   :     _options_.aliasKeywords = rhs; break;
            case 60   :     _options_.collapseProperties = rhs; break;
            case 61   :     _options_.devirtualizePrototypeMethods = rhs; break;
            case  62  :     _options_.computeFunctionSideEffects = rhs; break;
            case  63  :    _options_.disambiguateProperties = rhs; break;
            case 64   :     _options_.ambiguateProperties = rhs; break;
            case  65  :     _options_.exportTestFunctions = rhs; break;
            case  66  :     _options_.instrumentForCoverage = rhs; break;
            case  67  :     _options_.instrumentForCoverageOnly = rhs; break;
            case  68  :     _options_.ignoreCajaProperties = rhs; break;
            case  69  :     _options_.markAsCompiled = rhs; break;
            case  70  :     _options_.removeTryCatchFinally = rhs; break;
            case  71  :     _options_.closurePass = rhs; break;
            case  72  :     _options_.gatherCssNames = rhs; break;
            case  73  :     _options_.moveFunctionDeclarations = rhs; break;
            case  74  :     _options_.recordFunctionInformation = rhs; break;
            case   75 :     _options_.generateExports = rhs; break;
            case  76  :     _options_.prettyPrint = rhs; break;
            case  77  :     _options_.lineBreak = rhs; break;
        }
    }
    private CompilerOptions _options_ ;

    /** Added by Sketch4J ***/
    private boolean getRHS(int rid) {
        switch (rid) {
            case  0  : return flags.print_tree ;
            case  1  : return flags.compute_phase_ordering ;
            case  2  : return flags.print_ast;
            case  3  : return flags.print_pass_graph ;
            case  4  : return flags.create_name_map_files ;
            case  5  : return flags.third_party ;
            case   6 : return flags.use_only_custom_externs;
            case  7  : return flags.debug ;
            case  8  : return flags.process_closure_primitives ;
            case 10   :  return  _options_.ideMode;
            case 11   :  return  _options_.checkSymbols;
            case  12  :   return  _options_.checkDuplicateMessages;
            case  13  :   return  _options_.allowLegacyJsMessages;
            case 14: return _options_.printInputDelimiter;
            case 15   :   return  _options_.strictMessageReplacement;
            case  16  :   return  _options_.checkSuspiciousCode;
            case  17  :  return  _options_.checkControlStructures;
            case  18  :   return  _options_.checkUnusedPropertiesEarly;
            case  19  :  return  _options_.checkTypes;
            case  20  :  return  _options_.tightenTypes;
            case  21  :   return  _options_.inferTypesInGlobalScope;
            case  22  :   return  _options_.checkTypedPropertyCalls;
            case  23  :   return  _options_.checkEs5Strict;
            case  24  :   return  _options_.checkCaja;
            case   25 :  return  _options_.foldConstants;
            case  26  :   return  _options_.removeConstantExpressions;
            case  27  :   return  _options_.deadAssignmentElimination;
            case  28  :   return  _options_.inlineConstantVars;
            case  29  :   return  _options_.inlineFunctions;
            case  30  :   return  _options_.decomposeExpressions;
            case  31  :  return  _options_.inlineAnonymousFunctionExpressions;
            case  32  :  return  _options_.inlineLocalFunctions;
            case  33  :   return  _options_.crossModuleCodeMotion;
            case  34  :   return  _options_.coalesceVariableNames;
            case  35  :   return  _options_.crossModuleMethodMotion;
            case  36  :   return  _options_.inlineGetters;
            case  37  :   return  _options_.inlineVariables;
            case  38  :   return  _options_.inlineLocalVariables;
            case  39  :   return  _options_.flowSensitiveInlineVariables;   
            case  40  :   return  _options_.smartNameRemoval;
            case  41  :   return  _options_.removeDeadCode;
            case  42  :   return  _options_.extractPrototypeMemberDeclarations;
            case  43  :   return  _options_.removeEmptyFunctions;
            case  44  :   return  _options_.removeUnusedPrototypeProperties;
            case  45  :   return  _options_.removeUnusedPrototypePropertiesInExterns;
            case  46  :   return  _options_.removeUnusedVars;
            case 47   :   return  _options_.removeUnusedVarsInGlobalScope;
            case 48   :   return  _options_.aliasExternals;
            case  49  :  return  _options_.collapseVariableDeclarations;
            case 50   :   return  _options_.collapseAnonymousFunctions; 
            case  51  : return  _options_.aliasAllStrings;
            case  52  :   return  _options_.convertToDottedProperties;
            case 53   :   return  _options_.rewriteFunctionExpressions;
            case  54  :   return  _options_.optimizeParameters;
            case  55  :   return  _options_.optimizeArgumentsArray;
            case  56  :   return  _options_.labelRenaming; 
            case  57  :   return  _options_.reserveRawExports;
            case  58  :   return  _options_.generatePseudoNames; 
            case 59   :   return  _options_.aliasKeywords;
            case 60   :   return  _options_.collapseProperties;
            case 61   :   return  _options_.devirtualizePrototypeMethods;
            case  62  :   return  _options_.computeFunctionSideEffects;
            case  63  :  return  _options_.disambiguateProperties;
            case 64   :   return  _options_.ambiguateProperties;
            case  65  :   return  _options_.exportTestFunctions;
            case  66  :   return  _options_.instrumentForCoverage;
            case  67  :   return  _options_.instrumentForCoverageOnly;
            case  68  :   return  _options_.ignoreCajaProperties;
            case  69  :   return  _options_.markAsCompiled;
            case  70  :   return  _options_.removeTryCatchFinally;
            case  71  :   return  _options_.closurePass;
            case  72  :   return  _options_.gatherCssNames;
            case  73  :   return  _options_.moveFunctionDeclarations;
            case  74  :   return  _options_.recordFunctionInformation;
            case   75 :   return  _options_.generateExports;
            case  76  :   return  _options_.prettyPrint;
            case  77  :   return  _options_.lineBreak;

        }
        return false;
    }



    @Override
    protected Compiler createCompiler() {
        return new Compiler(getErrorPrintStream());
    }

    @Override
    protected List<JSSourceFile> createExterns() throws FlagUsageException, IOException {
        List<JSSourceFile> externs = super.createExterns();
        if (!flags.use_only_custom_externs) {
            List<JSSourceFile> defaultExterns = getDefaultExterns();
            defaultExterns.addAll(externs);
            return defaultExterns;
        } else {
            return externs;
        }
    }

    /**
     * @return a mutable list
     * @throws IOException
     */
    private List<JSSourceFile> getDefaultExterns() throws IOException {
        InputStream input = CommandLineRunner.class.getResourceAsStream("/externs.zip");
        ZipInputStream zip = new ZipInputStream(input);
        List<JSSourceFile> externs = Lists.newLinkedList();
        for (ZipEntry entry = null; (entry = zip.getNextEntry()) != null;) {
            LimitInputStream entryStream = new LimitInputStream(zip, entry.getSize());
            externs.add(JSSourceFile.fromInputStream(entry.getName(), entryStream));
        }
        return externs;
    }

    /**
     * Runs the Compiler. Exits cleanly in the event of an error.
     */
    public static void main(String[] args) {
        try {
            (new CommandLineRunner(args)).run();
        } catch (CmdLineException e) {
            System.exit(-1);
        }
    }
}
