package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.renderer.MethodRenderer;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.impl.ShadowApiImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodRendererImpl implements MethodRenderer
{

   private final Context context;
   private final Method method;

   public MethodRendererImpl(Method method)
   {
      this.context = ((ShadowApiImpl) method.getApi()).getRenderingContext();
      this.method = method;
   }

   public static String declaration(Context context, Method method, String content)
   {
      StringBuilder sb = new StringBuilder();

      if (!method.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(method.getDirectAnnotationUsages()
                         .stream()
                         .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                         .collect(Collectors.joining()));
      }
      Set<Modifier> modifiers = new HashSet<>(method.getModifiers());
      if (!content.isEmpty())
      {
         modifiers.remove(Modifier.ABSTRACT);
      }

      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      if (!method.getFormalGenerics().isEmpty())
      {
         sb.append('<');
         sb.append(method.getFormalGenerics()
                         .stream()
                         .map(generic -> ShadowRendererImpl.type(context, generic))
                         .collect(Collectors.joining(", ")));
         sb.append('>');
         sb.append(' ');
      }
      sb.append(ShadowRendererImpl.type(context, method.getReturnType()));
      sb.append(' ');
      sb.append(method.getSimpleName());
      sb.append('(');

      method.getReceiverType().ifPresent(declared ->
                                         {

                                            sb.append(ShadowRendererImpl.type(context, declared));
                                            sb.append(' ');
                                            sb.append(declared.getSimpleName());
                                            sb.append('.');
                                            sb.append("this");
                                            if (!method.getParameters().isEmpty())
                                            {
                                               sb.append(' ');
                                            }
                                         });

      if (!method.getParameters().isEmpty())
      {
         sb.append(method.getParameters()
                         .stream()
                         .map(parameter -> ParameterRendererImpl.declaration(context, parameter))
                         .collect(Collectors.joining(", ")));
      }
      sb.append(')');

      if (!method.getThrows().isEmpty())
      {
         sb.append(' ');
         sb.append("throws ");
         sb.append(method.getThrows()
                         .stream().map(aClass -> ShadowRendererImpl.type(context, aClass))
                         .collect(Collectors.joining(", ")));
      }

      if (method.isAbstract() && content.isBlank())
      {
         sb.append(';');
      }
      else
      {
         sb.append(" {");
         if (!content.isEmpty())
         {
            sb.append('\n');
            sb.append(content);
            if (!content.endsWith("\n"))
            {
               sb.append('\n');
            }
         }
         sb.append("}");
      }
      sb.append('\n');
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, method, "");
   }

   @Override
   public String declaration(String content)
   {
      return declaration(context, method, content);
   }

   @Override
   public String invocation()
   {
      return invocation("");
   }

   @Override
   public String invocation(String parameters)
   {
      return method.getSimpleName() +
             '(' +
             parameters +
             ')';
   }
}