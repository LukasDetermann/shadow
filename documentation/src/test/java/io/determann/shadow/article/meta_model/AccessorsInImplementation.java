package io.determann.shadow.article.meta_model;

public class AccessorsInImplementation
{
   //@formatter:off
//tag::core[]
interface Query<FROM, RESULT> {}

interface Method
{
   <RESULT> RESULT getOrThrow(Query<Method, RESULT> query);
}
//end::core[]

//tag::reflection[]
interface ReflectionQueries
{
   public static Query<Method, String> name()
   {
      return null;//to implement
   }

   public static Query<Method, FuncOp> funcOp()
   {
      return null;//to implement
   }
}
//end::reflection[]

//tag::annotation_processing[]
interface AnnotationProcessingQueries
{
   public static Query<Method, String> name()
   {
      return null;//to implement
   }
}
//end::annotation_processing[]

static {
//tag::usage[]
Method method = null;
String name = method.getOrThrow(ReflectionQueries.name());
//end::usage[]
//@formatter:on
}
}
