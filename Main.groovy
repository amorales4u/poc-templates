// https://mvnrepository.com/artifact/com.hubspot.jinjava/jinjava
@Grapes( [
    @Grab(group='com.hubspot.jinjava', module='jinjava', version='2.7.0'),
    @Grab(group='com.google.guava', module='guava', version='32.0.0-jre')
    ]
)

import groovy.text.GStringTemplateEngine
import com.google.common.base.CaseFormat
import groovy.io.FileType

class Main {

    static void main( String[] args ) {
        
        String var1 = "Project Name"
        println "Hello again..."
        println CaseFormat.LOWER_HYPHEN.to( CaseFormat.UPPER_CAMEL, var1 )
        
        def template = new Main()
        template.template()

    }
    
    def binding = new HashMap<String,Object>()
    
    def  methodMissing(String name, def args) {  
       println "Test..." 
      if( binding.containsKey( name ) ) {
            return binding.get( name )
      }      
      return binding.get( name )
    }  
    
    void template() {
        binding << [ "ProjectName": "Employee" ]
        binding << [ "projectName": "employee" ]
        binding << [ "ProjectPrefix": "Employee" ]
        binding << [ "projectPrefix": "employee" ]
        binding << [ "ProjectSuffix": "Service" ]
        binding << [ "projectSuffix": "service" ]
        binding << [ "artifact-id": "employee-service" ]
        binding << [ "artifact_id": "employee_service" ]
        binding << [ "ARTIFACT-ID": "EMPLOYEE-SERVICE" ]
        binding << [ "ARTIFACT_ID": "EMPLOYEE_SERVICE" ]
        binding << [ "root_directory": "com/palo/it/employee/service" ]
        binding << [ "root_package": "com.palo.it.employee.service"]
        binding << [ "group-id": "com.carfif.bra.purchase-service" ]
        binding << [ "eventModule": null ]
        
        binding << [ "com.main.package.name": '''com.palo.it.employee''']
        binding << [ "serverPort": 9010 ]
        binding << [ "_": this ]

        binding << [ "upperCase": { var -> 
        //println var 
        return var.toUpperCase()
        } ]


        def text = 'Dear "$projectName" $ProjectName '
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(text).make(binding)
        
        //println template.toString()
        def miniJinja = new MiniJinja().
                setBinding(binding)

        println miniJinja.render( " ProjectName: {{   ProjectName }} ")
        println miniJinja.render( " projectName: {{ projectName }} ")
        println miniJinja.render( " Main Package Name: {{ com.main.package.name }} ")
        println miniJinja.render( " No Var: {{ NotFoundVar }} ")
        println miniJinja.render( " Custom function: {{ projectName | upperCase }} ")
        println miniJinja.render( " Custom expression: {{ projectName + ' Hola mundo' }} ")
        println miniJinja.render( " Port: {{ serverPort }} ")
        println miniJinja.render( " Port: {{ serverPort + 1 }} ")

        def sourcePath = "/dev/projects/poc-template/layouts/layouts_base"
        def targetPath = "/dev/test-impl/from-template"
        def listFiles = []    
        def dir = new File(sourcePath)
        dir.eachFileRecurse (FileType.FILES) { file ->
            //println file.getCanonicalPath() - dir.getCanonicalPath()
            listFiles << file.getCanonicalPath() - dir.getCanonicalPath()
        }

        def templetedFile = new TempletedFile()
        listFiles.each {
            templetedFile.createFile( binding, sourcePath, targetPath, it )
        }

        println dir.getCanonicalPath()

        def username = System.console().readLine 'What is your name? '
        println "Hello $username"
        
    
    }

 

}
