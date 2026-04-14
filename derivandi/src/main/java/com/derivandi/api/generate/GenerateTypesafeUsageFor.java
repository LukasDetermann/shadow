package com.derivandi.api.generate;

import com.derivandi.api.Ap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// A Typesafe [Ap.AnnotationUsage] will be generated for listed Annotations.
/// Alternative see [GenerateTypesafeUsage]
/// @see GenerateTypesafeUsage
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface GenerateTypesafeUsageFor
{
   /// Annotations a Typesafe [Ap.AnnotationUsage] should be generated for
   /// @see GenerateTypesafeUsage
   Class<?>[] value();
}