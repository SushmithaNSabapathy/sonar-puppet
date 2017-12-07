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
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.puppet.api.PuppetCheck;
import org.sonar.plugins.puppet.api.visitors.CustomRulesDefinition;
import org.sonar.plugins.puppet.api.visitors.TreeVisitor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Checks {

  private final CheckFactory checkFactory;
  private Set<org.sonar.api.batch.rule.Checks> checksByRepository = Sets.newHashSet();

  private Checks(CheckFactory checkFactory) {
    this.checkFactory = checkFactory;
  }

  public static Checks createPuppetChecks(CheckFactory checkFactory) {
    return new Checks(checkFactory);
  }

  public Checks addChecks(String repositoryKey, Iterable<Class> checkClass) {
    checksByRepository.add(checkFactory
      .<PuppetCheck>create(repositoryKey)
      .addAnnotatedChecks(checkClass));

    return this;
  }

  public Checks addCustomChecks(@Nullable CustomRulesDefinition[] customRulesDefinitions) {
    if (customRulesDefinitions != null) {

      for (CustomRulesDefinition rulesDefinition : customRulesDefinitions) {
        addChecks(rulesDefinition.repositoryKey(), Lists.newArrayList(rulesDefinition.checkClasses()));
      }
    }
    return this;
  }

  public List<PuppetCheck> all() {
    List<PuppetCheck> allVisitors = Lists.newArrayList();

    for (org.sonar.api.batch.rule.Checks checks : checksByRepository) {
      allVisitors.addAll(checks.all());
    }

    return allVisitors;
  }

  public List<TreeVisitor> visitorChecks() {
    return all().stream()
      .filter(c -> c instanceof TreeVisitor)
      .map(c -> (TreeVisitor) c)
      .collect(Collectors.toList());
  }

  @Nullable
  public RuleKey ruleKeyFor(PuppetCheck check) {
    RuleKey ruleKey;

    for (org.sonar.api.batch.rule.Checks checks : checksByRepository) {
      ruleKey = checks.ruleKey(check);

      if (ruleKey != null) {
        return ruleKey;
      }
    }
    return null;
  }

}
