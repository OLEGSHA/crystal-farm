package ru.windcorp.tge2.util.lang;

import java.io.InputStream;
import java.util.Scanner;

import ru.windcorp.tge2.util.interfaces.Generator;

public class QuickLangSetup {
	
	public static void setupLang(final ClassLoader loader, final String path) {
		Lang.langGenerator = new Generator<String, InputStream>() {
			@Override
			public InputStream generate(String input) {
				return loader.getResourceAsStream(path + "/" + input + ".lang");
			}
		};
		
		Scanner scanner = new Scanner(loader.getResourceAsStream(path + "/langs"));
		
		while (scanner.hasNext()) {
			LangInfo.getLangInfo(scanner.next(), scanner.next());
		}
		
		scanner.close();
	}

}
