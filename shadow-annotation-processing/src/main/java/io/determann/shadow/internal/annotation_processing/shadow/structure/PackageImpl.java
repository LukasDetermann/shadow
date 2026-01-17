package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.packageInfo;
import static java.util.Optional.ofNullable;

public class PackageImpl implements Ap.Package
{
   private final PackageElement packageElement;
   private final Ap.Context context;
   private final NoType noType;

   public PackageImpl(Ap.Context context, PackageElement packageElement)
   {
      this.context = context;
      this.packageElement = packageElement;
      this.noType = (NoType) packageElement.asType();
   }

   @Override
   public String getQualifiedName()
   {
      return getElement().getQualifiedName().toString();
   }

   @Override
   public boolean isUnnamed()
   {
      return getElement().isUnnamed();
   }

   public PackageElement getElement()
   {
      return packageElement;
   }

   @Override
   public List<Ap.Declared> getDeclared()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .map(TypeElement.class::cast)
                         .map(typeElement -> Adapters.<Ap.Declared>adapt(getApi(), typeElement))
                         .toList();
   }

   @Override
   public Optional<Ap.Declared> getDeclared(String qualifiedName)
   {
      return getDeclared().stream()
                          .filter(declared -> declared.getQualifiedName().equals(qualifiedName))
                          .findFirst();
   }

   @Override
   public Ap.Module getModule()
   {
      return Adapters.adapt(getApi(), adapt(getApi()).toElements().getModuleOf(getElement()));
   }

   @Override
   public String getName()
   {
      return getElement().getSimpleName().toString();
   }

   @Override
   public Optional<String> getJavaDoc()
   {
      return ofNullable(adapt(getApi()).toElements().getDocComment(getElement()));
   }

   @Override
   public List<Ap.AnnotationUsage> getAnnotationUsages()
   {
      return Adapters.adapt(getApi(), adapt(getApi()).toElements().getAllAnnotationMirrors(getElement()));
   }

   @Override
   public List<Ap.AnnotationUsage> getDirectAnnotationUsages()
   {
      return adapt(getApi(), getElement().getAnnotationMirrors());
   }

   @Override
   public String renderPackageInfo(RenderingContext renderingContext)
   {
      return packageInfo().annotate(getDirectAnnotationUsages())
                          .name(getQualifiedName())
                          .renderPackageInfo(renderingContext);
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      return getQualifiedName();
   }

   @Override
   public String toString()
   {
      return getElement().toString();
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getQualifiedName(), getModule());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Ap.Package otherPackage))
      {
         return false;
      }
      return Objects.equals(getQualifiedName(), otherPackage.getQualifiedName()) &&
             Objects.equals(getModule(), otherPackage.getModule());
   }

   private Ap.Context getApi()
   {
      return context;
   }

   public NoType getMirror()
   {
      return noType;
   }
}
