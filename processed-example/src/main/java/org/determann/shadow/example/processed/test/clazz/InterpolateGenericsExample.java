package org.determann.shadow.example.processed.test.clazz;

public class InterpolateGenericsExample <A extends Comparable<B>, B extends Comparable<A>>
{
   static class IndependentGeneric<C>
   {

   }

   static class DependentGeneric<D extends E, E>
   {

   }
}
