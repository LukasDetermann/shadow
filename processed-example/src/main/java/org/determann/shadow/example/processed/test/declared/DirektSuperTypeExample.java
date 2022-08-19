package org.determann.shadow.example.processed.test.declared;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DirektSuperTypeExample
{
   @interface AnnotationNoParent
   {

   }

   static class ClassNoParent
   {

   }

   static abstract class ClassParent extends Number
   {

   }

   static abstract class ClassMixedParent extends Number implements Comparable<ClassMixedParent>,
                                                                    Consumer<ClassMixedParent>
   {

   }

   interface InterfaceNoParent
   {

   }

   interface InterfaceParent extends Comparable<InterfaceParent>,
                                     Consumer<InterfaceParent>
   {

   }

   enum EnumNoParent
   {

   }
   enum EnumMultiParent implements Consumer<EnumMultiParent>,
                                   Supplier<EnumMultiParent>
   {
      ;
      @Override
      public void accept(EnumMultiParent enumMultiParent)
      {

      }

      @Override
      public EnumMultiParent get()
      {
         return null;
      }
   }

   record RecordNoParent() {}

   record RecordMultiParent() implements Consumer<RecordMultiParent>,
                                         Supplier<RecordMultiParent> {
      @Override
      public void accept(RecordMultiParent recordMultiParent)
      {

      }

      @Override
      public RecordMultiParent get()
      {
         return null;
      }
   }
}
