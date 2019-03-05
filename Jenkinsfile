node('windows ') {
	def CleanupScript     = "NexusArtifactCleanup.groovy"
	withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'nex_id', usernameVariable: 'nexusUsername', passwordVariable: 'nexusPassword']])
    {
		stage ('checkout'){ 
			checkout scm
		}
		stage ('cleanup nexus'){
			if(isUnix()) {
				sh "chmod +x ${CleanupScript}"
				groovy "${env.WORKSPACE}\\${CleanupScript}"
			}
			else{
				groovy "${env.WORKSPACE}\\${CleanupScript}"
			}
		}
       }
}
