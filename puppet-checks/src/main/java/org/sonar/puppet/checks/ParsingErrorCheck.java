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

import com.sonar.sslr.api.RecognitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.puppet.PuppetCheckVisitor;
import org.sonar.squidbridge.AstScannerExceptionHandler;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

import java.io.PrintWriter;
import java.io.StringWriter;

@Rule(
  key = "ParsingError",
  priority = Priority.CRITICAL,
  name = "Parser failure")
@ActivatedByDefault
@SqaleConstantRemediation("30min")
public class ParsingErrorCheck extends PuppetCheckVisitor implements AstScannerExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ParsingErrorCheck.class);

  @Override
  public void processException(Exception e) {
    StringWriter exception = new StringWriter();
    e.printStackTrace(new PrintWriter(exception)); // NOSONAR(squid:S1148): printStackTrace intentionally used
    LOG.debug("Parsing error", e);
    addIssueOnFile(this, e.getMessage());
  }

  @Override
  public void processRecognitionException(RecognitionException e) {
    addIssue(e.getLine(), this, e.getMessage());
  }
}
