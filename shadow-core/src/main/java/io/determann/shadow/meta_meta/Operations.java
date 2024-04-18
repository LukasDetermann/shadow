package io.determann.shadow.meta_meta;

import io.determann.shadow.api.Nameable;

public interface Operations
{
   public static Operation<Nameable, String> NAME = new Operation<>("name");
}
