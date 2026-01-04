package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.type.ArrayType;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public final class ArrayImpl extends TypeImpl<ArrayType> implements Ap.Array
{

   public ArrayImpl(Ap.Context context, ArrayType arrayType)
   {
      super(context, arrayType);
   }

   @Override
   public boolean isSubtypeOf(Ap.Type type)
   {
      return adapt(getApi()).toTypes().isSubtype(adapt(type).toTypeMirror(), getMirror());
   }

   @Override
   public Ap.Type getComponentType()
   {
      return adapt(getApi(), getMirror().getComponentType());
   }

   @Override
   public List<Ap.Type> getDirectSuperTypes()
   {
      return adapt(getApi()).toTypes()
                       .directSupertypes(getMirror())
                       .stream()
                       .map(typeMirror1 -> adapt(getApi(), typeMirror1))
                       .toList();
   }

   @Override
   public Ap.Array asArray()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getArrayType(getMirror()));
   }

   @Override
   public Ap.Wildcard asExtendsWildcard()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getWildcardType(getMirror(), null));
   }

   @Override
   public Ap.Wildcard asSuperWildcard()
   {
      return adapt(getApi(), adapt(getApi()).toTypes().getWildcardType(null, getMirror()));
   }

   @Override
   public boolean representsSameType(Ap.Type type)
   {
      return equals(type);
   }

   @Override
   public Ap.Array erasure()
   {
      return adapt(getApi(), ((ArrayType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return getComponentType().renderName(renderingContext) + "[]";
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return renderType(renderingContext);
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Array array &&
             Objects.equals(getComponentType(), array.getComponentType());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getComponentType());
   }

   @Override
   public String toString()
   {
      return "Array{" +
             "componentType=" + getComponentType() +
             '}';
   }
}
