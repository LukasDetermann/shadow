package io.determann.shadow.consistency;

import javax.tools.FileObject;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class ConsistencyFileObject implements FileObject
{
   private final FileObject delegate;
   private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

   public ConsistencyFileObject(FileObject delegate) {this.delegate = delegate;}

   @Override
   public URI toUri()
   {
      return delegate.toUri();
   }

   @Override
   public String getName()
   {
      return delegate.getName();
   }

   @Override
   public InputStream openInputStream() throws IOException
   {
      return delegate.openInputStream();
   }

   @Override
   public OutputStream openOutputStream()
   {
      return outputStream;
   }

   @Override
   public Reader openReader(boolean ignoreEncodingErrors) throws IOException
   {
      return delegate.openReader(ignoreEncodingErrors);
   }

   @Override
   public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException
   {
      return delegate.getCharContent(ignoreEncodingErrors);
   }

   @Override
   public Writer openWriter()
   {
      return new OutputStreamWriter(openOutputStream());
   }

   @Override
   public long getLastModified()
   {
      return delegate.getLastModified();
   }

   @Override
   public boolean delete()
   {
      return delegate.delete();
   }

   public byte[] getContent()
   {
      return outputStream.toString().getBytes(StandardCharsets.UTF_8);
   }
}
