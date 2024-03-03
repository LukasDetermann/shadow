package io.determann.shadow.article.meta_model;

import java.util.List;
import java.util.Optional;

public class Inheritance
{
   //@formatter:off
//tag::core[]
interface Method
{
   String getName();
}
//end::core[]

//tag::reflection[]
interface ReflectionMethod extends Method
{
   Optional<FuncOp> getCodeModel();
}
//end::reflection[]

//tag::annotation_processing[]
interface AnnotationProcessingMethod extends Method {}
//end::annotation_processing[]

//tag::hierarchy[]
interface Class<METHOD extends Method>
{
   List<METHOD> getMethods();
}
//end::hierarchy[]
//@formatter:on
}