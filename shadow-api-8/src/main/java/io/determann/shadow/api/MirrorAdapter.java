package io.determann.shadow.api;

import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.impl.shadow.*;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.type.*;
import java.util.Collections;
import java.util.Set;

import static io.determann.shadow.api.modifier.Modifier.*;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

@JdkApi
public interface MirrorAdapter
{
   static AnnotationMirror getMirror(AnnotationUsage annotationUsage)
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationMirror();
   }

   static DeclaredType getType(Declared declared)
   {
      return ((DeclaredImpl) declared).getMirror();
   }

   static ArrayType getType(Array array)
   {
      return ((ArrayImpl) array).getMirror();
   }

   static ExecutableType getType(Executable executable)
   {
      return ((ExecutableImpl) executable).getMirror();
   }

   static TypeMirror getType(Shadow shadow)
   {
      return ((ShadowImpl) shadow).getMirror();
   }

   static TypeVariable getType(Generic generic)
   {
      return ((GenericImpl) generic).getMirror();
   }

   static IntersectionType getType(IntersectionType intersectionType)
   {
      return ((IntersectionImpl) intersectionType).getMirror();
   }

   static NullType getType(Null aNull)
   {
      return ((NullImpl) aNull).getMirror();
   }

   static NoType getType(Package aPackage)
   {
      return ((PackageImpl) aPackage).getMirror();
   }

   static PrimitiveType getType(Primitive primitive)
   {
      return ((PrimitiveImpl) primitive).getMirror();
   }

   static NoType getType(Void aVoid)
   {
      return ((VoidImpl) aVoid).getMirror();
   }

   static WildcardType getType(Wildcard wildcard)
   {
      return ((WildcardImpl) wildcard).getMirror();
   }

   public static Set<Modifier> getModifiers(Element element)
   {
      return element.getModifiers()
                    .stream()
                    .map(MirrorAdapter::mapModifier)
                    .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
   }

   public static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      switch (modifier)
      {
         case PUBLIC:
            return PUBLIC;
         case PROTECTED:
            return PROTECTED;
         case PRIVATE:
            return PRIVATE;
         case ABSTRACT:
            return ABSTRACT;
         case STATIC:
            return STATIC;
         case FINAL:
            return FINAL;
         case STRICTFP:
            return STRICTFP;
         case DEFAULT:
            return DEFAULT;
         case TRANSIENT:
            return TRANSIENT;
         case VOLATILE:
            return VOLATILE;
         case SYNCHRONIZED:
            return SYNCHRONIZED;
         case NATIVE:
            return NATIVE;
         default:
            throw new IllegalArgumentException();
      }
   }
}