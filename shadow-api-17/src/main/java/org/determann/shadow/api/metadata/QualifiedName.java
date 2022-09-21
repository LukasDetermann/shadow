package org.determann.shadow.api.metadata;

import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks qualifiedName parameter with IntelliJ integration.
 * e.g. org.example.MyClass
 */
@Pattern("^([a-zA-Z_$][a-zA-Z\\d_$]*\\.)*[a-zA-Z_$][a-zA-Z\\d_$]*$")
@Target(ElementType.PARAMETER)
public @interface QualifiedName
{
}