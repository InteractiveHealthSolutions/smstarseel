<%@page import="java.io.File"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%
final long  MEGABYTE = 1024L * 1024L;

System.gc();
System.out.println("GC GC GC");

StringBuilder sb11111=new StringBuilder();
sb11111.append("\n*********IN MB*******" +
		  "\nAVAILABLE MEMORY : "+Runtime.getRuntime().freeMemory()/MEGABYTE);
sb11111.append("\nTOTAL MEMORY   : "+Runtime.getRuntime().totalMemory()/MEGABYTE);
sb11111.append("\nMAXIMUM MEMORY : "+Runtime.getRuntime().maxMemory()/MEGABYTE);

System.out.println(sb11111.toString());

try {
	Thread.sleep(2000);
}
catch (InterruptedException e) {
	e.printStackTrace();
}

boolean isMultipart = ServletFileUpload.isMultipartContent(request);

if (isMultipart) {
    FileItemFactory factory = new DiskFileItemFactory();
    ServletFileUpload upload = new ServletFileUpload(factory);

    try {
        List items = upload.parseRequest(request);
        Iterator iterator = items.iterator();
        while (iterator.hasNext()) {
            FileItem item = (FileItem) iterator.next();
            if (!item.isFormField()) {
                String fileName = item.getName();
                String root = "d:\\TEST";
                
                // Make DIRs if needed
                File path = new File(root + "/fileuploads");
                if (!path.exists()) {
                    boolean status = path.mkdirs();
                }
                
                //File uploaded on path created above
                File uploadedFile = new File(path +"/"+ fileName);
                
                BufferedInputStream in = null;
                FileOutputStream fout = null;
                try
                {
                    in = new BufferedInputStream(item.getInputStream());
                    fout = new FileOutputStream(uploadedFile);
         
                    byte data[] = new byte[1024];
                    int count;
                    while ((count = in.read(data, 0, 1024)) != -1)
                    {
                        fout.write(data, 0, count);
                        System.out.println(count);
                    }
                }
                finally
                {
                    if (in != null)
                            in.close();
                    if (fout != null)
                            fout.close();
                }
                System.out.println("Done");
            }
        }
    } catch (Exception e) {
    	e.printStackTrace();
    }
}

System.gc();
System.out.println("GC GC GC");

StringBuilder sb1=new StringBuilder();
sb1.append("\n*********IN MB*******" +
		  "\nAVAILABLE MEMORY : "+Runtime.getRuntime().freeMemory()/MEGABYTE);
sb1.append("\nTOTAL MEMORY   : "+Runtime.getRuntime().totalMemory()/MEGABYTE);
sb1.append("\nMAXIMUM MEMORY : "+Runtime.getRuntime().maxMemory()/MEGABYTE);

System.out.println(sb1.toString());

try {
	Thread.sleep(2000);
}
catch (InterruptedException e) {
	e.printStackTrace();
}

%>