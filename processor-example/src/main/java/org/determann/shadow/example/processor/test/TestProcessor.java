package org.determann.shadow.example.processor.test;


import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.ShadowProcessor;
import org.determann.shadow.example.processor.test.shadow.*;
import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class TestProcessor extends ShadowProcessor
{
   //give all the classes called by junit access to the shadow-api
   public static ShadowApi SHADOW_API;
   private static boolean isFirstRound = true;

   @Override
   public void process(ShadowApi shadowApi) throws Exception
   {
      SHADOW_API = shadowApi;

      //one test once
      if (!isFirstRound)
      {
         return;
      }
      isFirstRound = false;

      LauncherConfig launcherConfig = LauncherConfig.builder()
                                                    //spi can get complicated when called by the maven compiler plugin. so hardcode the engine
                                                    .enableTestEngineAutoRegistration(false)
                                                    .addTestEngines(new JupiterTestEngine())
                                                    .build();

      LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                                                                        //fileSelectors do get resolved in junit-platform-launcher 1.8.*
                                                                        //dirSelector, uriSelector, packageSelector have classloader problems
                                                                        .selectors(
                                                                              selectClass(ShadowApiTest.class),
                                                                              selectClass(ElementBackedTest.class),
                                                                              selectClass(AnnotationUsageTest.class),
                                                                              selectClass(AnnotationableTest.class),

                                                                              selectClass(AnnotationTest.class),
                                                                              selectClass(ClassTest.class),
                                                                              selectClass(EnumTest.class),
                                                                              selectClass(InterfaceTest.class),
                                                                              selectClass(RecordTest.class),

                                                                              selectClass(ArrayTest.class),

                                                                              selectClass(ConstructorTest.class),
                                                                              selectClass(MethodTest.class),

                                                                              selectClass(IntersectionTest.class),
                                                                              selectClass(VoidTest.class),
                                                                              selectClass(ModuleTest.class),
                                                                              selectClass(PackageTest.class),
                                                                              selectClass(RecordComponentTest.class),
                                                                              selectClass(NullTest.class),
                                                                              selectClass(PrimitiveTest.class),
                                                                              selectClass(GenericTest.class),
                                                                              selectClass(WildcardTest.class),

                                                                              selectClass(EnumConstantTest.class),
                                                                              selectClass(FieldTest.class),
                                                                              selectClass(ParameterTest.class))
                                                                        .build();

      SummaryGeneratingListener listener = new SummaryGeneratingListener();
      LauncherFactory.create(launcherConfig).execute(request, listener);

      TestExecutionSummary summary = listener.getSummary();

      StringWriter summaryWriter = new StringWriter();
      summary.printTo(new PrintWriter(summaryWriter));

      if (summary.getTotalFailureCount() != 0)
      {
         StringWriter errorPrinter = new StringWriter();
         summary.printFailuresTo(new PrintWriter(errorPrinter));
         shadowApi.logError(errorPrinter.toString());
         shadowApi.logWarning(summaryWriter.toString());
      }
      else
      {
         String implementedTests = new DecimalFormat("0.00")
               .format((double) summary.getTestsSucceededCount() / summary.getTestsFoundCount() * 100);
         shadowApi.logWarning(summaryWriter + "Tests implemented: " + implementedTests + "%");
      }
   }
}
