package net.pterodactylus.util.envopt;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * Parses values from an {@link Environment} into {@link Option}-annotated fields of an object.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class Parser {

	private final Environment environment;

	public Parser(Environment environment) {
		this.environment = environment;
	}

	public <T> T parseEnvironment(Supplier<T> optionsObjectSupplier) {
		T optionsObject = optionsObjectSupplier.get();
		Class<?> optionsClass = optionsObject.getClass();
		for (Field field : optionsClass.getDeclaredFields()) {
			Option[] options = field.getAnnotationsByType(Option.class);
			if (options.length == 0) {
				continue;
			}
			for (Option option : options) {
				String variableName = option.value();
				String value = environment.getValue(variableName).orElse(null);
				field.setAccessible(true);
				try {
					field.set(optionsObject, value);
				} catch (IllegalAccessException iae1) {
					/* swallow. */
				}
			}
		}
		return optionsObject;
	}

}
