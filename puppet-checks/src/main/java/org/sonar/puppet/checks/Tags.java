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

public class Tags {
  public static final String BRAIN_OVERLOAD = "brain-overload";
  public static final String BUG = "bug";
  public static final String CONFUSING = "confusing";
  public static final String CONVENTION = "convention";
  public static final String FUTURE_PARSER = "future-parser";
  public static final String OBSOLETE = "obsolete";
  public static final String SECURITY = "security";
  public static final String PITFALL = "pitfall";
  public static final String UNUSED = "unused";

  private Tags() {
    // This class only defines constants
  }
}
