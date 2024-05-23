package io.determann.shadow.meta_meta;

import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Wildcard;

public interface Operations
{
   public static Operation<Nameable, String> NAMEABLE_NAME = new Operation<>("nameable.name");

   public static Operation<Wildcard, Shadow> WILDCARD_EXTENDS = new Operation<>("wildcard.extends");

   public static Operation<Wildcard, Shadow> WILDCARD_SUPER = new Operation<>("wildcard.super");
}
