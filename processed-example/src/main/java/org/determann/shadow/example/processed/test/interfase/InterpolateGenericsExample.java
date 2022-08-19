package org.determann.shadow.example.processed.test.interfase;

public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>
{
   interface IndependentGeneric<C>
   {

   }

   interface DependentGeneric<D extends E, E>
   {

   }
}
