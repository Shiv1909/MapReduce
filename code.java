import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mappe
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class VisitorCount {

    public static class VisitorMapper extends Mapper<LongWritable, Text, Text, LongWritable> {     
    	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {   
       		 String line = value.toString();           
       	 	String[] words = line.split("\\s+");      
       	 	String visitor = words[0];               
       	 	context.write(new Text(visitor), new LongWritable(1));  
 	  	 }
    }

    public static class VisitorReducer extends Reducer<Text, LongWritable, Text, LongWritable> {    

 	   public void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {    
       	 long sum = 0;
        	for (LongWritable value : values) {
           	 	sum += value.get();
       	 }                                                          
       	context.write(key, new LongWritable(sum));                    
   	 }
    }

    public static void main(String[] args) throws Exception {      
       	Configuration conf = new Configuration();
       	Job job = Job.getInstance(conf, "Visitor Count");           
 
       	job.setJarByClass(VisitorCount.class);                  
       	job.setMapperClass(VisitorMapper.class);
       	job.setReducerClass(VisitorReducer.class);              

      	job.setInputFormatClass(TextInputFormat.class);
       	job.setOutputFormatClass(TextOutputFormat.class);          

      	job.setOutputKeyClass(Text.class);
      	job.setOutputValueClass(LongWritable.class);              

      	TextInputFormat.setInputPaths(job, new Path(args[0]));
      	TextOutputFormat.setOutputPath(job, new Path(args[1]));    
      	System.exit(job.waitForCompletion(true) ? 0 : 1);          
 	}
}


