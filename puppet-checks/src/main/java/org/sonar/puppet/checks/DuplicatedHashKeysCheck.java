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

import com.google.common.collect.Lists;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.puppet.PuppetGrammar;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

import java.util.List;

@Rule(
  key = "DuplicatedHashKeys",
  name = "Duplicated hash keys should be removed",
  priority = Priority.CRITICAL,
  tags = {Tags.BUG})
@SqaleConstantRemediation("5min")
@ActivatedByDefault
public class DuplicatedHashKeysCheck extends PuppetCheckVisitor {

  private List<String> keys = Lists.newArrayList();

  @Override
  public void init() {
    subscribeTo(PuppetGrammar.HASH_PAIRS);
  }

  @Override
  public void visitNode(AstNode paramsNode) {
    keys.clear();
    for (AstNode hashPairNode : paramsNode.getChildren(PuppetGrammar.HASH_PAIR)) {
      if (keys.contains(hashPairNode.getFirstChild(PuppetGrammar.KEY).getTokenValue())) {
        addIssue(
          hashPairNode.getFirstChild(PuppetGrammar.KEY),
          this,
          "Remove the duplicated key \"" + hashPairNode.getFirstChild(PuppetGrammar.KEY).getTokenValue() + "\".");
      } else {
        keys.add(hashPairNode.getFirstChild(PuppetGrammar.KEY).getTokenValue());
      }
    }
  }
}
