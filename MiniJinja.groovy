import com.google.common.base.CaseFormat
import groovy.util.Eval


class MiniJinja {

    def binding = new HashMap<String,Object>()
    String openKey = "{{"
    String closeKey = "}}"
    int openKeySize = 2
    int closeKeySize = 2
        
    def setBinding( def binding ) {
        this.binding = binding
        return this
    } 
    def render( String sourceTemplate ) {
        String template = sourceTemplate
        
        int startIdx = template.indexOf(openKey) 
        
        while( startIdx != -1 ) {
            int endIdx = template.indexOf(closeKey) 
            String varName = template.substring( startIdx + openKeySize, endIdx - ( closeKeySize - 1 ) )
            varName = varName.trim()
            /*
            println "Find value of [$varName]"
            println "[$template]"
            println "[" + template.substring( 0, startIdx ) + "]"
            println "[" + template.substring( endIdx + keySize ) + "]"
            */
            def functionName = null
            if( varName.indexOf("|") != -1 ) {
                def varAndFunction = varName.split("\\|")
                //println varAndFunction
                varName = varAndFunction[0].trim()
                functionName = varAndFunction[1].trim()
            }
            def isExpression = varName.split(" ")
            def value = null
            if( isExpression.length > 1 ) {
                value = evaluateExpression( varName, isExpression )
            } else {
                value = binding[ varName ]
            }
            
            if( functionName != null ) {
                value = evalFunction( value , functionName )
            }
            template = template.substring( 0, startIdx ) +
                       value +
                       template.substring(endIdx + closeKeySize)

            startIdx = template.indexOf(openKey)

        }
        
        return template
    }
    
    def evalFunction( def value, String functionName ) {
        def functionDef = binding[ functionName ]

        if( functionDef == null ) {
            return functionName + " not found"
        }
        return functionDef.call( value )
    }

    def evaluateExpression(String expression, splitedExpression) {

        def expressionToEval = ""

        splitedExpression.each { it -> 
            if( binding.containsKey( it ) ) {
                def constant = binding.get(it)
                def arround = ""
                if( constant instanceof String ) {
                    arround = "'"
                } 
                expressionToEval += arround + binding.get(it) + arround
            } else {
                expressionToEval += " " + it
            }
        }

        return Eval.me(expressionToEval)

    }

}