/*******************************************************************************
 * Copyright (c) 2014-2016 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.application.properties.metadata.completions;

import org.eclipse.lsp4j.CompletionItemKind;
import org.springframework.ide.vscode.application.properties.metadata.PropertyInfo;
import org.springframework.ide.vscode.application.properties.metadata.hints.ValueHintHoverInfo;
import org.springframework.ide.vscode.application.properties.metadata.types.Type;
import org.springframework.ide.vscode.application.properties.metadata.types.TypeParser;
import org.springframework.ide.vscode.application.properties.metadata.types.TypeUtil;
import org.springframework.ide.vscode.application.properties.metadata.types.TypedProperty;
import org.springframework.ide.vscode.application.properties.metadata.util.FuzzyMap.Match;
import org.springframework.ide.vscode.commons.languageserver.completion.DocumentEdits;
import org.springframework.ide.vscode.commons.languageserver.completion.ICompletionProposal;
import org.springframework.ide.vscode.commons.languageserver.completion.ScoreableProposal;
import org.springframework.ide.vscode.commons.languageserver.java.JavaProjectFinder;
import org.springframework.ide.vscode.commons.languageserver.util.IDocument;
import org.springframework.ide.vscode.commons.yaml.schema.YType;

public class PropertyCompletionFactory {

	
	public ICompletionProposal valueProposal(String value, String query, Type type, double score, DocumentEdits edits, ValueHintHoverInfo info) {
		return new ScoreableProposal() {
			
			@Override
			public DocumentEdits getTextEdit() {
				return edits;
			}
			
			@Override
			public String getLabel() {
				return value;
			}
			
			@Override
			public CompletionItemKind getKind() {
				return CompletionItemKind.Value;
			}
			
			@Override
			public double getBaseScore() {
				return score;
			}
		};
	}

	public ScoreableProposal property(IDocument doc, DocumentEdits applier, Match<PropertyInfo> prop, TypeUtil typeUtil) {
		return new PropertyProposal(doc, applier, prop, typeUtil);
	}

	public ScoreableProposal beanProperty(IDocument doc, final String contextProperty, final Type contextType, final String pattern, final TypedProperty property, final double score, DocumentEdits applier, final TypeUtil typeUtil) {
		AbstractPropertyProposal proposal = new AbstractPropertyProposal(doc, applier) {

//			private HoverInfo hoverInfo;


//			@Override
//			public HoverInfo getAdditionalProposalInfo(IProgressMonitor monitor) {
//				if (hoverInfo==null) {
//					String prefix = contextProperty==null?"":contextProperty+".";
//					hoverInfo = new JavaTypeNavigationHoverInfo(prefix+property.getName(), property.getName(), contextType, property.getType(), typeUtil);
//				}
//				return hoverInfo;
//			}

			@Override
			protected String getBaseDisplayString() {
				return property.getName();
			}

			@Override
			protected String getHighlightPattern() {
				return pattern;
			}

			@Override
			protected Type getType() {
				return property.getType();
			}

			@Override
			public double getBaseScore() {
				return score;
			}

			@Override
			protected String niceTypeName(YType type) {
				return typeUtil.niceTypeName((Type) type);
			}

			@Override
			public String getLabel() {
				return getBaseDisplayString() + " : " + typeUtil.niceTypeName(getType());
			}
			
		};
		if (property.isDeprecated()) {
			proposal.deprecate();
		}
		return proposal;
	}

	private JavaProjectFinder documentContextFinder;

	public PropertyCompletionFactory(JavaProjectFinder documentContextFinder) {
		this.documentContextFinder = documentContextFinder;
	}

	private class PropertyProposal extends AbstractPropertyProposal {
		private Match<PropertyInfo> match;
		private Type type;
		private TypeUtil typeUtil;

		public PropertyProposal(IDocument doc, DocumentEdits applier, Match<PropertyInfo> match,
				TypeUtil typeUtil) {
			super(doc, applier);
			this.typeUtil = typeUtil;
			this.match = match;
			if (match.data.isDeprecated()) {
				deprecate();
			}
		}

//		@Override
//		public HoverInfo getAdditionalProposalInfo(IProgressMonitor monitor) {
//			return new SpringPropertyHoverInfo(documentContextFinder.getJavaProject(fDoc), match.data);
//		}

		@Override
		protected String getBaseDisplayString() {
			return match.data.getId();
		}

		@Override
		public double getBaseScore() {
			return match.score;
		}

		@Override
		protected Type getType() {
			if (type==null) {
				type = TypeParser.parse(match.data.getType());
			}
			return type;
		}

		@Override
		protected String getHighlightPattern() {
			return match.getPattern();
		}

		@Override
		protected String niceTypeName(YType type) {
			return typeUtil.niceTypeName(((Type)type));
		}

		@Override
		public String getLabel() {
			return getBaseDisplayString() + " : " + typeUtil.niceTypeName(getType());
		}
		
	}

}