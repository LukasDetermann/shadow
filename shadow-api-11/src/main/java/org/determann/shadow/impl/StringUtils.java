package org.determann.shadow.impl;

import java.util.function.Predicate;
import java.util.regex.Pattern;

class StringUtils
{

   private static final Predicate<String> SCREAMING_SNAKE_CASE = Pattern.compile("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$").asMatchPredicate();
   private static final Predicate<String> LOWER_CAMEL_CASE = Pattern.compile("^[a-z][a-zA-Z0-9]*$").asMatchPredicate();
   private static final Predicate<String> UPPER_CAMEL_CASE = Pattern.compile("^[A-Z][a-zA-Z0-9]*$").asMatchPredicate();

   private StringUtils()
   {
   }

   static String to_UpperCamelCase(String toModify)
   {
      toModify = extractAfterDot(toModify);

      if (UPPER_CAMEL_CASE.test(toModify))
      {
         return toModify;
      }
      if (SCREAMING_SNAKE_CASE.test(toModify))
      {
         StringBuilder result = new StringBuilder();
         boolean capitaliseNext = true;
         for (char c : toModify.toCharArray())
         {
            if (c == '_')
            {
               capitaliseNext = true;
               continue;
            }
            if (capitaliseNext)
            {
               result.append(Character.toUpperCase(c));
               capitaliseNext = false;
               continue;
            }
            result.append(Character.toLowerCase(c));
         }
         return result.toString();
      }
      if (LOWER_CAMEL_CASE.test(toModify))
      {
         return Character.toUpperCase(toModify.charAt(0)) + toModify.substring(1);
      }
      throw new IllegalArgumentException("Unknown pattern. cant convert \"" + toModify + "\" to UpperCamelCase");
   }

   static String to_lowerCamelCase(String toModify)
   {
      toModify = extractAfterDot(toModify);

      if (LOWER_CAMEL_CASE.test(toModify))
      {
         return toModify;
      }
      if (SCREAMING_SNAKE_CASE.test(toModify))
      {
         StringBuilder result = new StringBuilder();
         boolean capitaliseNext = false;
         for (char c : toModify.toCharArray())
         {
            if (c == '_')
            {
               capitaliseNext = true;
               continue;
            }
            if (capitaliseNext)
            {
               result.append(c);
               capitaliseNext = false;
               continue;
            }
            result.append(Character.toLowerCase(c));
         }
         return result.toString();
      }
      if (UPPER_CAMEL_CASE.test(toModify))
      {
         return Character.toLowerCase(toModify.charAt(0)) + toModify.substring(1);
      }
      throw new IllegalArgumentException("Unknown pattern. cant convert \"" + toModify + "\" to lowerCamelCase");
   }

   static String to_SCREAMING_SNAKE_CASE(String toModify)
   {
      toModify = extractAfterDot(toModify);

      if (SCREAMING_SNAKE_CASE.test(toModify))
      {
         return toModify;
      }
      if (UPPER_CAMEL_CASE.test(toModify) || LOWER_CAMEL_CASE.test(toModify))
      {
         StringBuilder result = new StringBuilder();
         for (char c : toModify.toCharArray())
         {
            if (result.length() != 0 && Character.isUpperCase(c))
            {
               result.append("_").append(c);
               continue;
            }
            result.append(Character.toUpperCase(c));
         }

         return result.toString();
      }
      throw new IllegalArgumentException("Unknown pattern. cant convert \"" + toModify + "\" to SCREAMING_CAMEL_CASE");
   }

   private static String extractAfterDot(String toModify)
   {
      int dotIndex = toModify.lastIndexOf(".");
      if (dotIndex == -1)
      {
         return toModify;
      }
      return toModify.substring(dotIndex + 1);
   }
}
