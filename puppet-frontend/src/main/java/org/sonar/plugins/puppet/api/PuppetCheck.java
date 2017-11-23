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
package org.sonar.plugins.puppet.api;

import org.sonar.puppet.tree.Tree;
import org.sonar.plugins.puppet.api.visitors.TreeVisitorContext;
import org.sonar.plugins.puppet.api.visitors.issues.FileIssue;
import org.sonar.plugins.puppet.api.visitors.issues.Issue;
import org.sonar.plugins.puppet.api.visitors.issues.LineIssue;
import org.sonar.plugins.puppet.api.visitors.issues.PreciseIssue;

import java.util.List;

public interface PuppetCheck {

  PreciseIssue addPreciseIssue(Tree tree, String message);

  FileIssue addFileIssue(String message);

  LineIssue addLineIssue(int line, String message);

  List<Issue> scanFile(TreeVisitorContext context);

  void validateParameters();

}
