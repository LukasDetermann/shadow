package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ModuleRendererTest
{

   private static final String EXPECTED = "module java.desktop {\n" +
                                          "requires java.prefs;\n" +
                                          "requires transitive java.datatransfer;\n" +
                                          "requires transitive java.xml;\n" +
                                          "\n" +
                                          "exports java.applet;\n" +
                                          "exports java.awt;\n" +
                                          "exports java.awt.color;\n" +
                                          "exports java.awt.desktop;\n" +
                                          "exports java.awt.dnd;\n" +
                                          "exports java.awt.event;\n" +
                                          "exports java.awt.font;\n" +
                                          "exports java.awt.geom;\n" +
                                          "exports java.awt.im;\n" +
                                          "exports java.awt.im.spi;\n" +
                                          "exports java.awt.image;\n" +
                                          "exports java.awt.image.renderable;\n" +
                                          "exports java.awt.print;\n" +
                                          "exports java.beans;\n" +
                                          "exports java.beans.beancontext;\n" +
                                          "exports javax.accessibility;\n" +
                                          "exports javax.imageio;\n" +
                                          "exports javax.imageio.event;\n" +
                                          "exports javax.imageio.metadata;\n" +
                                          "exports javax.imageio.plugins.bmp;\n" +
                                          "exports javax.imageio.plugins.jpeg;\n" +
                                          "exports javax.imageio.plugins.tiff;\n" +
                                          "exports javax.imageio.spi;\n" +
                                          "exports javax.imageio.stream;\n" +
                                          "exports javax.print;\n" +
                                          "exports javax.print.attribute;\n" +
                                          "exports javax.print.attribute.standard;\n" +
                                          "exports javax.print.event;\n" +
                                          "exports javax.sound.midi;\n" +
                                          "exports javax.sound.midi.spi;\n" +
                                          "exports javax.sound.sampled;\n" +
                                          "exports javax.sound.sampled.spi;\n" +
                                          "exports javax.swing;\n" +
                                          "exports javax.swing.border;\n" +
                                          "exports javax.swing.colorchooser;\n" +
                                          "exports javax.swing.event;\n" +
                                          "exports javax.swing.filechooser;\n" +
                                          "exports javax.swing.plaf;\n" +
                                          "exports javax.swing.plaf.basic;\n" +
                                          "exports javax.swing.plaf.metal;\n" +
                                          "exports javax.swing.plaf.multi;\n" +
                                          "exports javax.swing.plaf.nimbus;\n" +
                                          "exports javax.swing.plaf.synth;\n" +
                                          "exports javax.swing.table;\n" +
                                          "exports javax.swing.text;\n" +
                                          "exports javax.swing.text.html;\n" +
                                          "exports javax.swing.text.html.parser;\n" +
                                          "exports javax.swing.text.rtf;\n" +
                                          "exports javax.swing.tree;\n" +
                                          "exports javax.swing.undo;\n" +
                                          "exports java.awt.dnd.peer to jdk.unsupported.desktop;\n" +
                                          "exports sun.awt to jdk.accessibility, jdk.unsupported.desktop;\n" +
                                          "exports sun.awt.dnd to jdk.unsupported.desktop;\n" +
                                          "exports sun.swing to jdk.unsupported.desktop;\n" +
                                          "\n" +
                                          "opens com.sun.java.swing.plaf.windows to jdk.jconsole;\n" +
                                          "opens javax.swing.plaf.basic to jdk.jconsole;\n" +
                                          "\n" +
                                          "uses java.awt.im.spi.InputMethodDescriptor;\n" +
                                          "uses javax.accessibility.AccessibilityProvider;\n" +
                                          "uses javax.imageio.spi.ImageInputStreamSpi;\n" +
                                          "uses javax.imageio.spi.ImageOutputStreamSpi;\n" +
                                          "uses javax.imageio.spi.ImageReaderSpi;\n" +
                                          "uses javax.imageio.spi.ImageTranscoderSpi;\n" +
                                          "uses javax.imageio.spi.ImageWriterSpi;\n" +
                                          "uses javax.print.PrintServiceLookup;\n" +
                                          "uses javax.print.StreamPrintServiceFactory;\n" +
                                          "uses javax.sound.midi.spi.MidiDeviceProvider;\n" +
                                          "uses javax.sound.midi.spi.MidiFileReader;\n" +
                                          "uses javax.sound.midi.spi.MidiFileWriter;\n" +
                                          "uses javax.sound.midi.spi.SoundbankReader;\n" +
                                          "uses javax.sound.sampled.spi.AudioFileReader;\n" +
                                          "uses javax.sound.sampled.spi.AudioFileWriter;\n" +
                                          "uses javax.sound.sampled.spi.FormatConversionProvider;\n" +
                                          "uses javax.sound.sampled.spi.MixerProvider;\n" +
                                          "uses sun.swing.InteropProvider;\n" +
                                          "\n" +
                                          "provides java.net.ContentHandlerFactory with sun.awt.www.content.MultimediaContentHandlers;\n" +
                                          "provides javax.print.PrintServiceLookup with sun.print.PrintServiceLookupProvider;\n" +
                                          "provides javax.print.StreamPrintServiceFactory with sun.print.PSStreamPrinterFactory;\n" +
                                          "provides javax.sound.midi.spi.MidiDeviceProvider with com.sun.media.sound.MidiInDeviceProvider, com.sun.media.sound.MidiOutDeviceProvider, com.sun.media.sound.RealTimeSequencerProvider, com.sun.media.sound.SoftProvider;\n" +
                                          "provides javax.sound.midi.spi.MidiFileReader with com.sun.media.sound.StandardMidiFileReader;\n" +
                                          "provides javax.sound.midi.spi.MidiFileWriter with com.sun.media.sound.StandardMidiFileWriter;\n" +
                                          "provides javax.sound.midi.spi.SoundbankReader with com.sun.media.sound.AudioFileSoundbankReader, com.sun.media.sound.DLSSoundbankReader, com.sun.media.sound.JARSoundbankReader, com.sun.media.sound.SF2SoundbankReader;\n" +
                                          "provides javax.sound.sampled.spi.AudioFileReader with com.sun.media.sound.AiffFileReader, com.sun.media.sound.AuFileReader, com.sun.media.sound.SoftMidiAudioFileReader, com.sun.media.sound.WaveFileReader, com.sun.media.sound.WaveFloatFileReader, com.sun.media.sound.WaveExtensibleFileReader;\n" +
                                          "provides javax.sound.sampled.spi.AudioFileWriter with com.sun.media.sound.AiffFileWriter, com.sun.media.sound.AuFileWriter, com.sun.media.sound.WaveFileWriter, com.sun.media.sound.WaveFloatFileWriter;\n" +
                                          "provides javax.sound.sampled.spi.FormatConversionProvider with com.sun.media.sound.AlawCodec, com.sun.media.sound.AudioFloatFormatConverter, com.sun.media.sound.PCMtoPCMCodec, com.sun.media.sound.UlawCodec;\n" +
                                          "provides javax.sound.sampled.spi.MixerProvider with com.sun.media.sound.DirectAudioDeviceProvider, com.sun.media.sound.PortMixerProvider;\n" +
                                          "provides sun.datatransfer.DesktopDatatransferService with sun.awt.datatransfer.DesktopDatatransferServiceImpl;\n" +
                                          " }\n";

   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals(EXPECTED, render(shadowApi.getModuleOrThrow("java.desktop")).declaration()))
                   .compile();
   }
}