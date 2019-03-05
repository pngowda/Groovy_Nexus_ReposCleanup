node('windows ') {
	def CleanupScript     = "NexusArtifactCleanup.groovy"
	
		stage ('checkout'){ 
			checkout scm
		}
		stage ('cleanup nexus'){
			if(isUnix()) {
				sh "chmod +x ${CleanupScript}"
				sh "${env.WORKSPACE}\\${CleanupScript}"
			}
			else{
				bat "${env.WORKSPACE}\\${CleanupScript}"
			}
		}
	
}
