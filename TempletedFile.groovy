class TempletedFile {
    def miniJinja = new MiniJinja()
    def engine = new groovy.text.GStringTemplateEngine()
    def binding

    def createFile( def binding, String sourcePath, String targetPath, def fileName ) {
        this.binding = binding
        miniJinja.setBinding(binding)
        
        File newDirectoryStructureParent = new File(targetPath)
        def newFileName = miniJinja.render(fileName)
        
        def newContainer = new File(targetPath + newFileName).getParent()
        def newDir = new File(newContainer)
        newDir.mkdirs()

        createFile( sourcePath + fileName, targetPath + newFileName )

    }

    def createFile( def sourceFileName, def targetFileName ) {
        //println "Read from: $sourceFileName"
        //println " Write to: $targetFileName"
        
        File sourceFile = new File( sourceFileName )
        String fileTemplate = sourceFile.text

        fileTemplate = miniJinja.render(fileTemplate)

        File targetFile = new File( targetFileName )
        if( targetFile.exists() ) {
            targetFile.text = ""
        }
        fileTemplate = secondTemplateFace(fileTemplate, sourceFileName)
        targetFile << fileTemplate

    }

    def secondTemplateFace( String stringFileTemplate, String templateFileName ) {
        try {
            def template = engine.createTemplate(stringFileTemplate).make(binding)
            return template.toString() 
        } catch( Exception ex ) {
            println "Error at create: $templateFileName"
            println ex.message
            return stringFileTemplate
        }
    }
}
