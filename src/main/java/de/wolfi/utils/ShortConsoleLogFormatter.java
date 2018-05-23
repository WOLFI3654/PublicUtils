package de.wolfi.utils;
/*    */ 
/*    */ import java.io.StringWriter;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.logging.LogRecord;
/*    */ 
/*    */ public class ShortConsoleLogFormatter extends java.util.logging.Formatter
/*    */ {
/*    */   private final SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
/*    */   
/*    */  
/*    */   
/*    */   public String format(LogRecord record)
/*    */   {
/* 42 */     StringBuilder builder = new StringBuilder();
/* 43 */     Throwable ex = record.getThrown();
/*    */     
/* 45 */    
/* 46 */     builder.append(" [");
/* 47 */     builder.append(record.getLevel().getLocalizedName().toUpperCase());
/* 48 */     builder.append("] ");
builder.append(date.format(Long.valueOf(record.getMillis())));
/* 49 */     builder.append(formatMessage(record));
/* 50 */     builder.append('\n');
/*    */     
/* 52 */     if (ex != null) {
/* 53 */       StringWriter writer = new StringWriter();
/* 54 */       ex.printStackTrace(new java.io.PrintWriter(writer));
/* 55 */       builder.append(writer);
/*    */     }
/*    */     
/* 58 */     return builder.toString();
/*    */   }
/*    */ }

/* Location:           C:\Users\root\Desktop\Eclispe\APIs\craftbukkit.jar
 * Qualified Name:     org.bukkit.craftbukkit.v1_8_R3.util.ShortConsoleLogFormatter
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.7.1
 */