package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.lang_model.shadow.ExecutableImpl;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;

public interface AnnotationProcessingAdapter
{
   static AnnotationMirror particularize(AnnotationUsage annotationUsage)
   {
      return LangModelAdapter.particularize(annotationUsage);
   }

   static DeclaredType particularize(Declared declared)
   {
      return LangModelAdapter.particularType(declared);
   }

   static ArrayType particularType(Array array)
   {
      return LangModelAdapter.particularType(array);
   }

   static ExecutableType particularType(Executable executable)
   {
      return LangModelAdapter.particularType(executable);
   }

   static TypeMirror particularType(Shadow shadow)
   {
      return LangModelAdapter.particularType(shadow);
   }

   static TypeVariable particularType(Generic generic)
   {
      return LangModelAdapter.particularType(generic);
   }

   static IntersectionType particularType(Intersection intersection)
   {
      return LangModelAdapter.particularType(intersection);
   }

   static NoType particularType(io.determann.shadow.api.shadow.Module module)
   {
      return LangModelAdapter.particularType(module);
   }

   static NullType particularType(Null aNull)
   {
      return LangModelAdapter.particularType(aNull);
   }

   static NoType particularType(io.determann.shadow.api.shadow.Package aPackage)
   {
      return LangModelAdapter.particularType(aPackage);
   }

   static PrimitiveType particularType(Primitive primitive)
   {
      return LangModelAdapter.particularType(primitive);
   }

   static NoType particularType(Void aVoid)
   {
      return LangModelAdapter.particularType(aVoid);
   }

   static WildcardType particularType(Wildcard wildcard)
   {
      return LangModelAdapter.particularType(wildcard);
   }

   static Element particularElement(Annotationable annotationable)
   {
      return LangModelAdapter.particularElement(annotationable);
   }

   static TypeElement particularElement(Declared declared)
   {
      return LangModelAdapter.particularElement(declared);
   }

   static ExecutableElement particularElement(Executable executable)
   {
      return LangModelAdapter.particularElement(executable);
   }

   static TypeParameterElement particularElement(Generic generic)
   {
      return LangModelAdapter.particularElement(generic);
   }

   static ModuleElement particularElement(io.determann.shadow.api.shadow.Module module)
   {
      return LangModelAdapter.particularElement(module);
   }

   static PackageElement particularElement(Package aPackage)
   {
      return LangModelAdapter.particularElement(aPackage);
   }

   static RecordComponentElement particularElement(RecordComponent recordComponent)
   {
      return LangModelAdapter.particularElement(recordComponent);
   }

   static VariableElement particularElement(Variable variable)
   {
      return LangModelAdapter.particularElement(variable);
   }

   static io.determann.shadow.api.modifier.Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      return LangModelAdapter.mapModifier(modifier);
   }

   static Executable generalize(AnnotationProcessingContext context, ExecutableElement element)
   {
      return new ExecutableImpl(context, element);
   }

   /**
    * {@link Element}s represent a usage. so for example a field may have the type {@code List<String>}. When you want the resulting {@link Shadow}
    * to represent {@code List<String>} and not just {@code List<T>} use the {@link Element} to create it.
    *
    * @see #generalize(AnnotationProcessingContext, TypeMirror)
    */
   static <SHADOW extends Shadow> SHADOW generalize(AnnotationProcessingContext context, Element element)
   {
      return LangModelAdapter.generalize(context, element);
   }

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #generalize(AnnotationProcessingContext, Element)
    */
   static <SHADOW extends Shadow> SHADOW generalize(AnnotationProcessingContext context, TypeMirror typeMirror)
   {
      return LangModelAdapter.generalize(context, typeMirror);
   }

   static List<AnnotationUsage> generalize(AnnotationProcessingContext context, List<? extends AnnotationMirror> annotationMirrors)
   {
      return LangModelAdapter.generalize(context, annotationMirrors);
   }

   static javax.lang.model.element.AnnotationValue generalize(AnnotationValue annotationValue)
   {
      return LangModelAdapter.generalize(annotationValue);
   }

   static Types getTypes(AnnotationProcessingContext context)
   {
      return LangModelAdapter.getTypes(context);
   }

   static Elements getElements(AnnotationProcessingContext context)
   {
      return LangModelAdapter.getElements(context);
   }
}
