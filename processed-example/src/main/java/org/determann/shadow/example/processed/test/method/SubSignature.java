package org.determann.shadow.example.processed.test.method;

import java.util.List;

public abstract class SubSignature
{
   public abstract void first();

   public abstract void second();

   public abstract void third(String name, Long id);

   public abstract void four(Long id, String name);

   public abstract void five(Long id, String name2);

   public abstract void six(List list);

   public abstract void seven(List<String> strings);
}
