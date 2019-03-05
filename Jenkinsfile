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
				sh "groovy ${env.WORKSPACE}\\${CleanupScript}"
			}
			else{
				sh "groovy ${env.WORKSPACE}\\${CleanupScript}"
			}
		}
       }
}
