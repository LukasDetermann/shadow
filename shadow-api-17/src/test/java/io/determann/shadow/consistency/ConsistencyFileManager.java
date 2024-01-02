package io.determann.shadow.consistency;

import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;

class ConsistencyFileManager implements StandardJavaFileManager
{
   private final StandardJavaFileManager delegate;
   private final Map<String, byte[]> files = new HashMap<>();

   ConsistencyFileManager(StandardJavaFileManager delegate) {this.delegate = delegate;}

   @Override
   public ClassLoader getClassLoader(Location location)
   {
      return delegate.getClassLoader(location);
   }

   @Override
   public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse)
         throws IOException
   {
      return delegate.list(location, packageName, kinds, recurse);
   }

   @Override
   public String inferBinaryName(Location location, JavaFileObject file)
   {
      return delegate.inferBinaryName(location, file);
   }

   @Override
   public boolean isSameFile(FileObject a, FileObject b)
   {
      return delegate.isSameFile(a, b);
   }

   @Override
   public boolean handleOption(String current, Iterator<String> remaining)
   {
      return delegate.handleOption(current, remaining);
   }

   @Override
   public boolean hasLocation(Location location)
   {
      return delegate.hasLocation(location);
   }

   @Override
   public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException
   {
      return delegate.getJavaFileForInput(location, className, kind);
   }

   @Override
   public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
   {
      return new SimpleJavaFileObject(URI.create(className), kind)
      {
         @Override
         public ByteArrayOutputStream openOutputStream()
         {
            return new ByteArrayOutputStream()
            {
               @Override
               public void close() throws IOException
               {
                  super.close();
                  files.put(className, toByteArray());
               }
            };
         }
      };
   }

   @Override
   public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException
   {
      return delegate.getFileForInput(location, packageName, relativeName);
   }

   @Override
   public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException
   {
      ConsistencyFileObject fileObject = new ConsistencyFileObject(delegate.getFileForOutput(location,
                                                                                             packageName,
                                                                                             relativeName,
                                                                                             sibling));
      files.put(relativeName, fileObject.getContent());
      return fileObject;
   }

   @Override
   public void flush() throws IOException
   {
      delegate.flush();
   }

   @Override
   public void close() throws IOException
   {
      delegate.close();
   }

   @Override
   public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files)
   {
      return delegate.getJavaFileObjectsFromFiles(files);
   }

   @Override
   public Iterable<? extends JavaFileObject> getJavaFileObjects(File... files)
   {
      return delegate.getJavaFileObjects(files);
   }

   @Override
   public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names)
   {
      return delegate.getJavaFileObjectsFromStrings(names);
   }

   @Override
   public Iterable<? extends JavaFileObject> getJavaFileObjects(String... names)
   {
      return delegate.getJavaFileObjects(names);
   }

   @Override
   public void setLocation(Location location, Iterable<? extends File> files) throws IOException
   {
      delegate.setLocation(location, files);
   }

   @Override
   public Iterable<? extends File> getLocation(Location location)
   {
      return delegate.getLocation(location);
   }

   @Override
   public int isSupportedOption(String option)
   {
      return delegate.isSupportedOption(option);
   }

   @Override
   public Location getLocationForModule(Location location, String moduleName) throws IOException
   {
      return delegate.getLocationForModule(location, moduleName);
   }

   @Override
   public Location getLocationForModule(Location location, JavaFileObject fo) throws IOException
   {
      return delegate.getLocationForModule(location, fo);
   }

   @Override
   public <S> ServiceLoader<S> getServiceLoader(Location location, Class<S> service) throws IOException
   {
      return delegate.getServiceLoader(location, service);
   }

   @Override
   public String inferModuleName(Location location) throws IOException
   {
      return delegate.inferModuleName(location);
   }

   @Override
   public Iterable<Set<Location>> listLocationsForModules(Location location) throws IOException
   {
      return delegate.listLocationsForModules(location);
   }

   @Override
   public boolean contains(Location location, FileObject fo) throws IOException
   {
      return delegate.contains(location, fo);
   }

   public Map<String, byte[]> getCompiledClasses()
   {
      return files;
   }
}
