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
package org.sonar.plugins.puppet;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.puppet.api.CustomPuppetRulesDefinition;
import org.sonar.plugins.puppet.api.PuppetCheck;
import org.sonar.plugins.puppet.api.visitors.TreeVisitor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PuppetChecks {

  private final CheckFactory checkFactory;
  private Set<Checks<PuppetCheck>> checksByRepository = Sets.newHashSet();

  private PuppetChecks(CheckFactory checkFactory) {
    this.checkFactory = checkFactory;
  }

  public static PuppetChecks createPuppetCheck(CheckFactory checkFactory) {
    return new PuppetChecks(checkFactory);
  }

  public PuppetChecks addChecks(String repositoryKey, Iterable<Class> checkClass) {
    checksByRepository.add(checkFactory
      .<PuppetCheck>create(repositoryKey)
      .addAnnotatedChecks(checkClass));

    return this;
  }

  public PuppetChecks addCustomChecks(@Nullable CustomPuppetRulesDefinition[] customRulesDefinitions) {
    if (customRulesDefinitions != null) {

      for (CustomPuppetRulesDefinition rulesDefinition : customRulesDefinitions) {
        addChecks(rulesDefinition.repositoryKey(), Lists.newArrayList(rulesDefinition.checkClasses()));
      }
    }
    return this;
  }

  public List<PuppetCheck> all() {
    List<PuppetCheck> allVisitors = Lists.newArrayList();

    for (Checks<PuppetCheck> checks : checksByRepository) {
      allVisitors.addAll(checks.all());
    }

    return allVisitors;
  }

  public List<TreeVisitor> visitorChecks() {
    List<TreeVisitor> checks = new ArrayList<>();
    for (PuppetCheck check : all()) {
      if (check instanceof TreeVisitor) {
        checks.add((TreeVisitor) check);
      }
    }

    return checks;
  }

  @Nullable
  public RuleKey ruleKeyFor(PuppetCheck check) {
    RuleKey ruleKey;

    for (Checks<PuppetCheck> checks : checksByRepository) {
      ruleKey = checks.ruleKey(check);

      if (ruleKey != null) {
        return ruleKey;
      }
    }
    return null;
  }

}
