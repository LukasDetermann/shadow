package io.determann.shadow.consistency.test;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import static javax.tools.JavaFileObject.Kind.SOURCE;

public class ConsistencyTest<SUT>
{
   private final Function<AnnotationProcessingContext, SUT> compileTimeExecutable;
   private Function<Function<String, Class<?>>, SUT> runtimeExecutable;
   private final List<JavaFileObject> toCompile;

   public ConsistencyTest(Function<AnnotationProcessingContext, SUT> compileTime,
                          Function<Function<String, Class<?>>, SUT> runtime,
                          List<JavaFileObject> toCompile)
   {
      this.compileTimeExecutable = compileTime;
      this.runtimeExecutable = runtime;
      this.toCompile = toCompile;
   }

   private ConsistencyTest(Function<AnnotationProcessingContext, SUT> compileTime)
   {
      this.compileTimeExecutable = Objects.requireNonNull(compileTime);
      this.toCompile = new ArrayList<>();
   }

   public static <SUT> ConsistencyTest<SUT> compileTime(Function<AnnotationProcessingContext, SUT> compileTime)
   {
      return new ConsistencyTest<>(compileTime);
   }

   public ConsistencyTest<SUT> runtime(Function<Function<String, Class<?>>, SUT> runtime)
   {
      this.runtimeExecutable = runtime;
      return this;
   }

   public ConsistencyTest<SUT> withCode(String fileName, String code)
   {
      toCompile.add(createJavaFileObject(createUri(fileName), code));
      return this;
   }

   public void test(Consumer<SUT> test)
   {
      test(test, test);
   }

   public void test(Consumer<SUT> compileTimeTest, Consumer<SUT> runTimeTest)
   {
      Map<String, byte[]> compiledClasses = new ConsistencyProcessor(context -> compileTimeTest.accept(compileTimeExecutable.apply(context)),
                                                                     toCompile).compile();

      defineClasses(compiledClasses);

      runTimeTest.accept(runtimeExecutable.apply(name ->
                                                 {
                                                    try
                                                    {
                                                       return Thread.currentThread().getContextClassLoader().loadClass(name);
                                                    }
                                                    catch (ClassNotFoundException e)
                                                    {
                                                       throw new RuntimeException(e);
                                                    }
                                                 }));
   }

   private void defineClasses(Map<String, byte[]> compiledClasses)
   {
      Thread.currentThread().setContextClassLoader(new ClassLoader()
      {
         @Override
         protected Class<?> findClass(String name) throws ClassNotFoundException
         {
            byte[] bytes = compiledClasses.get(name);
            if (bytes != null)
            {
               return defineClass(name, bytes, 0, bytes.length);
            }
            return super.findClass(name);
         }
      });
   }

   private SimpleJavaFileObject createJavaFileObject(URI uri, String sourceCode)
   {
      return new SimpleJavaFileObject(uri, SOURCE)
      {
         @Override
         public CharSequence getCharContent(boolean b)
         {
            return sourceCode;
         }
      };
   }

   private URI createUri(String fileName)
   {
      try
      {
         return compileTimeExecutable.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().resolve(fileName);
      }
      catch (SecurityException | URISyntaxException e)
      {
         return URI.create(fileName);
      }
   }
}

