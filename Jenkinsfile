node('master') {
	def CleanupScript     = "NexusArtifactCleanup.groovy"
	withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'nex_id', usernameVariable: 'nexusUsername', passwordVariable: 'nexusPassword']])
    {
		stage ('checkout'){ 
			checkout scm
		}
		stage ('cleanup nexus'){
			if(isUnix()) {
				sh "chmod +x ${CleanupScript}"
				def code = load 'NexusArtifactCleanup.groovy'
			}
			else{
				def code = load 'NexusArtifactCleanup.groovy'
			}
		}
       }
}
