/*
 * SonarQube Puppet Analyzer
 * Copyright (C) 2017-2017 David RACODON
 * david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.puppet.checks;

import java.util.regex.Pattern;

public class CheckStringUtils {

  private static final Pattern PATTERN_CONTAINING_VARIABLE_ENCLOSED_IN_BRACES = Pattern.compile(".*(?<!\\\\)\\$\\{(::)?(\\w+::)*\\w+}.*", Pattern.DOTALL);
  private static final Pattern PATTERN_CONTAINING_VARIABLE_NOT_ENCLOSED_IN_BRACES = Pattern.compile(".*(?<!\\\\)\\$(::)?(\\w+::)*\\w+.*", Pattern.DOTALL);
  private static final Pattern PATTERN_CONTAINING_ONLY_VARIABLE_ENCLOSED_IN_BRACES = Pattern.compile("(?<!\\\\)\\$\\{(::)?(\\w+::)*\\w+}");
  private static final Pattern PATTERN_CONTAINING_ONLY_VARIABLE_NOT_ENCLOSED_IN_BRACES = Pattern.compile("(?<!\\\\)\\$(::)?(\\w+::)*\\w+");
  private static final Pattern PATTERN_CONTAINING_SPECIAL_CHARACTER = Pattern.compile("\"|\\\\t|\\\\r|\\\\n|'|\\\\\\$");

  private CheckStringUtils() {
  }

  public static boolean containsVariable(String string) {
    return PATTERN_CONTAINING_VARIABLE_ENCLOSED_IN_BRACES.matcher(string).matches()
      || PATTERN_CONTAINING_VARIABLE_NOT_ENCLOSED_IN_BRACES.matcher(string).matches();
  }

  public static boolean containsNotEnclosedVariable(String string) {
    return PATTERN_CONTAINING_VARIABLE_NOT_ENCLOSED_IN_BRACES.matcher(string).matches();
  }

  public static boolean containsOnlyVariable(String string) {
    return PATTERN_CONTAINING_ONLY_VARIABLE_ENCLOSED_IN_BRACES.matcher(string).matches()
      || PATTERN_CONTAINING_ONLY_VARIABLE_NOT_ENCLOSED_IN_BRACES.matcher(string).matches();
  }

  public static boolean containsSpecialCharacter(String string) {
    return PATTERN_CONTAINING_SPECIAL_CHARACTER.matcher(string).find();
  }

}
