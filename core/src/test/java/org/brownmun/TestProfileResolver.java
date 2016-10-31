package org.brownmun;

import org.springframework.test.context.ActiveProfilesResolver;

public class TestProfileResolver implements ActiveProfilesResolver
{
	private boolean isCI()
	{
		return System.getenv("TRAVIS") != null;
	}

	@Override
	public String[] resolve(Class<?> testClass)
	{
		return new String[]{ "test", isCI() ? "ci" : "local" };
	}
}
