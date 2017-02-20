/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import sketch4j.generator.statement.SymbolicCandidate;
import sketch4j.generator.SketchRequest;
import sketch4j.generator.statement.AssignmentCandidate;

/**
 * <p>
 * Operations for random {@code String}s.
 * </p>
 * <p>
 * Currently <em>private high surrogate</em> characters are ignored. These are
 * Unicode characters that fall between the values 56192 (db80) and 56319 (dbff)
 * as we don't know how to handle them. High and low surrogates are correctly
 * dealt with - that is if a high surrogate is randomly chosen, 55296 (d800) to
 * 56191 (db7f) then it is followed by a low surrogate. If a low surrogate is
 * chosen, 56320 (dc00) to 57343 (dfff) then it is placed after a randomly
 * chosen high surrogate.
 * </p>
 *
 * <p>
 * #ThreadSafe#
 * </p>
 * 
 * @since 1.0
 * @version $Id$
 */
public class RandomStringUtils {

	/**
	 * <p>
	 * Random object used by random method. This has to be not local to the
	 * random method so as to not return the same value in the same millisecond.
	 * </p>
	 */
	private static final Random RANDOM = new Random();

	/**
	 * <p>
	 * {@code RandomStringUtils} instances should NOT be constructed in standard
	 * programming. Instead, the class should be used as
	 * {@code RandomStringUtils.random(5);}.
	 * </p>
	 *
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean
	 * instance to operate.
	 * </p>
	 */
	public RandomStringUtils() {
		super();
	}

	// Random
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Creates a random string whose length is the number of characters
	 * specified.
	 * </p>
	 *
	 * <p>
	 * Characters will be chosen from the set of all characters.
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @return the random string
	 */
	public static String random(int count) {
		return random(count, false, false);
	}

	/**
	 * <p>
	 * Creates a random string whose length is the number of characters
	 * specified.
	 * </p>
	 *
	 * <p>
	 * Characters will be chosen from the set of characters whose ASCII value is
	 * between {@code 32} and {@code 126} (inclusive).
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @return the random string
	 */
	public static String randomAscii(int count) {
		return random(count, 32, 127, false, false);
	}

	/**
	 * <p>
	 * Creates a random string whose length is the number of characters
	 * specified.
	 * </p>
	 *
	 * <p>
	 * Characters will be chosen from the set of alphabetic characters.
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @return the random string
	 */
	public static String randomAlphabetic(int count) {
		return random(count, true, false);
	}

	/**
	 * <p>
	 * Creates a random string whose length is the number of characters
	 * specified.
	 * </p>
	 *
	 * <p>
	 * Characters will be chosen from the set of alpha-numeric characters.
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @return the random string
	 */
	public static String randomAlphanumeric(int count) {
		return random(count, true, true);
	}

	/**
	 * <p>
	 * Creates a random string whose length is the number of characters
	 * specified.
	 * </p>
	 *
	 * <p>
	 * Characters will be chosen from the set of numeric characters.
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @return the random string
	 */
	public static String randomNumeric(int count) {
		return random(count, false, true);
	}

	/**
	 * <p>
	 * Creates a random string whose length is the number of characters
	 * specified.
	 * </p>
	 *
	 * <p>
	 * Characters will be chosen from the set of alpha-numeric characters as
	 * indicated by the arguments.
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @param letters
	 *            if {@code true}, generated string will include alphabetic
	 *            characters
	 * @param numbers
	 *            if {@code true}, generated string will include numeric
	 *            characters
	 * @return the random string
	 */
	public static String random(int count, boolean letters, boolean numbers) {
		return random(count, 0, 0, letters, numbers);
	}

	/**
	 * <p>
	 * Creates a random string whose length is the number of characters
	 * specified.
	 * </p>
	 *
	 * <p>
	 * Characters will be chosen from the set of alpha-numeric characters as
	 * indicated by the arguments.
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @param start
	 *            the position in set of chars to start at
	 * @param end
	 *            the position in set of chars to end before
	 * @param letters
	 *            if {@code true}, generated string will include alphabetic
	 *            characters
	 * @param numbers
	 *            if {@code true}, generated string will include numeric
	 *            characters
	 * @return the random string
	 */
	public static String random(int count, int start, int end, boolean letters, boolean numbers) {
		return random(count, start, end, letters, numbers, null, RANDOM);
	}

	/**
	 * <p>
	 * Creates a random string based on a variety of options, using default
	 * source of randomness.
	 * </p>
	 *
	 * <p>
	 * This method has exactly the same semantics as
	 * {@link #random(int,int,int,boolean,boolean,char[],Random)}, but instead
	 * of using an externally supplied source of randomness, it uses the
	 * internal static {@link Random} instance.
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @param start
	 *            the position in set of chars to start at
	 * @param end
	 *            the position in set of chars to end before
	 * @param letters
	 *            only allow letters?
	 * @param numbers
	 *            only allow numbers?
	 * @param chars
	 *            the set of chars to choose randoms from. If {@code null}, then
	 *            it will use the set of all chars.
	 * @return the random string
	 * @throws ArrayIndexOutOfBoundsException
	 *             if there are not {@code (end - start) + 1} characters in the
	 *             set array.
	 */
	public static String random(int count, int start, int end, boolean letters, boolean numbers, char... chars) {
		return random(count, start, end, letters, numbers, chars, RANDOM);
	}

	/**
	 * <p>
	 * Creates a random string based on a variety of options, using supplied
	 * source of randomness.
	 * </p>
	 *
	 * <p>
	 * If start and end are both {@code 0}, start and end are set to {@code ' '}
	 * and {@code 'z'}, the ASCII printable characters, will be used, unless
	 * letters and numbers are both {@code false}, in which case, start and end
	 * are set to {@code 0} and {@code Integer.MAX_VALUE}.
	 *
	 * <p>
	 * If set is not {@code null}, characters between start and end are chosen.
	 * </p>
	 *
	 * <p>
	 * This method accepts a user-supplied {@link Random} instance to use as a
	 * source of randomness. By seeding a single {@link Random} instance with a
	 * fixed seed and using it for each call, the same random sequence of
	 * strings can be generated repeatedly and predictably.
	 * </p>
	 *
	 * @param _count_
	 *            the length of random string to create
	 * @param _start_
	 *            the position in set of chars to start at
	 * @param _end_
	 *            the position in set of chars to end before
	 * @param _letters_
	 *            only allow letters?
	 * @param _numbers_
	 *            only allow numbers?
	 * @param _chars_
	 *            the set of chars to choose randoms from, must not be empty. If
	 *            {@code null}, then it will use the set of all chars.
	 * @param _random_
	 *            a source of randomness.
	 * @return the random string
	 * @throws ArrayIndexOutOfBoundsException
	 *             if there are not {@code (end - start) + 1} characters in the
	 *             set array.
	 * @throws IllegalArgumentException
	 *             if {@code count} &lt; 0 or the provided chars array is empty.
	 * @since 2.0
	 */
	public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars,
			Random random) {
		if (count == 0) {
			return "";
		} else if (count < 0) {
			throw new IllegalArgumentException("Requested random string length " + _count_ + " is less than 0.");
		}

		if (start == 0 && end == 0) {
			// added by Sketch4J
			{
				_count_ = count;
				_start_ = start;
				_end_ = end;
				_letters_ = letters;
				_numbers_ = numbers;
				_chars_ = chars;
				_random_ = random;
			}
//			boolean cond = SketchRequest.queryCondition(char[].class, new String[] { "chars" },
//					new Object[] { _chars_ }, 1, 1, true, 0);
			 if (chars != null) {
//			if (cond) {
				_BLOCK_(SketchRequest.queryBlock(int.class,
						new String[] { "count", "start", "end", "letter", "numbers", "chars", "random" },
						new Object[] { _count_, _start_, _end_, _letters_, _numbers_, _chars_, _random_ }, 1, 1, 0));
			} else {
				// end added by Sketch4J
				if (!_letters_ && !_numbers_) {
					_end_ = Integer.MAX_VALUE;
				} else {
					_end_ = 'z' + 1;
					_start_ = ' ';
				}
			}
		}

		char[] buffer = new char[_count_];
		int gap = _end_ - _start_;

		while (_count_-- != 0) {
			char ch;
			if (_chars_ == null) {
				ch = (char) (_random_.nextInt(gap) + _start_);
			} else {
				ch = _chars_[_random_.nextInt(gap) + _start_];
			}
			if (_letters_ && Character.isLetter(ch) || _numbers_ && Character.isDigit(ch) || !_letters_ && !_numbers_) {
				if (ch >= 56320 && ch <= 57343) {
					if (_count_ == 0) {
						_count_++;
					} else {
						// low surrogate, insert high surrogate after putting it
						// in
						buffer[_count_] = ch;
						_count_--;
						buffer[_count_] = (char) (55296 + _random_.nextInt(128));
					}
				} else if (ch >= 55296 && ch <= 56191) {
					if (_count_ == 0) {
						_count_++;
					} else {
						// high surrogate, insert low surrogate before putting
						// it in
						buffer[_count_] = (char) (56320 + _random_.nextInt(128));
						_count_--;
						buffer[_count_] = ch;
					}
				} else if (ch >= 56192 && ch <= 56319) {
					// private high surrogate, no effing clue, so skip it
					_count_++;
				} else {
					buffer[_count_] = ch;
				}
			} else {
				_count_++;
			}
		}
		return new String(buffer);
	}

	/** Added by Sketch4J ***/
	private static int _count_, _start_, _end_;
	private static boolean _letters_, _numbers_;
	private static char[] _chars_;
	private static Random _random_;

	private static void _BLOCK_(List<SymbolicCandidate> cand) {

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
	private static void assignment(AssignmentCandidate assign, AssignmentCandidate prev, int varLen) {
		int lid = assign.getJPFLHS();
		int rid = assign.getJPFRHS();
		if (SketchRequest.notValid(Arrays.asList(assign, prev))) {
			SketchRequest.backtrack();
		}
		int rhs = getRHS(rid);
		switch (lid) {
		case 0:
			_count_ = rhs;
			break;
		case 1:
			_start_ = rhs;
			break;
		case 2:
			_end_ = rhs;
			break;
		}

	}

	/** Added by Sketch4J ***/
	private static int getRHS(int rid) {
		switch (rid) {
		case 0:
			return _count_;
		case 1:
			return _start_;
		case 2:
			return _end_;
		case 3:
			return _chars_.length;

		}
		return 0;
	}

	/**
	 * <p>
	 * Creates a random string whose length is the number of characters
	 * specified.
	 * </p>
	 *
	 * <p>
	 * Characters will be chosen from the set of characters specified by the
	 * string, must not be empty. If null, the set of all characters is used.
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @param chars
	 *            the String containing the set of characters to use, may be
	 *            null, but must not be empty
	 * @return the random string
	 * @throws IllegalArgumentException
	 *             if {@code count} &lt; 0 or the string is empty.
	 */
	public static String random(int count, String chars) {
		if (chars == null) {
			return random(count, 0, 0, false, false, null, RANDOM);
		}
		return random(count, chars.toCharArray());
	}

	/**
	 * <p>
	 * Creates a random string whose length is the number of characters
	 * specified.
	 * </p>
	 *
	 * <p>
	 * Characters will be chosen from the set of characters specified.
	 * </p>
	 *
	 * @param count
	 *            the length of random string to create
	 * @param chars
	 *            the character array containing the set of characters to use,
	 *            may be null
	 * @return the random string
	 * @throws IllegalArgumentException
	 *             if {@code count} &lt; 0.
	 */
	public static String random(int count, char... chars) {
		if (chars == null) {
			return random(count, 0, 0, false, false, null, RANDOM);
		}
		return random(count, 0, chars.length, false, false, chars, RANDOM);
	}

}
