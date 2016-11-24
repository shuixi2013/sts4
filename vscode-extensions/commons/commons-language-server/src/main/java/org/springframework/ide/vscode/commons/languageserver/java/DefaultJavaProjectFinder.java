package org.springframework.ide.vscode.commons.languageserver.java;

import org.springframework.ide.vscode.commons.java.IJavaProject;
import org.springframework.ide.vscode.commons.languageserver.util.IDocument;
import org.springframework.ide.vscode.commons.util.Log;

public class DefaultJavaProjectFinder implements JavaProjectFinder {

	private final IJavaProjectFinderStrategy[] STRATEGIES = new IJavaProjectFinderStrategy[] {
		new MavenProjectFinderStrategy(),
		new JavaProjectWithClasspathFileFinderStrategy()
	};

	@Override
	public IJavaProject find(IDocument d) {
		for (IJavaProjectFinderStrategy strategy : STRATEGIES) {
			try {
				IJavaProject project = strategy.find(d);
				if (project != null) {
					return project;
				}
			} catch (Exception e) {
				Log.log(e);
			}
		}
		return null;
	}
}