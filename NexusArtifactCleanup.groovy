
import groovyx.net.http.*;
import static groovyx.net.http.ContentType.*;
import static groovyx.net.http.Method.*;
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )

class NexusArtifactCleanup {

	/**
	 *  Settings in which to run script.
	 */
	def settings = [
		baseUrl: 'https://projects.itemis.de/nexus',
		repositoryId: 'de.itemis.mps.build',
		pattern: '.*',
		age: 0,
		nexusUsername: System.getenv('nexusUsername'),
		nexusPassword: System.getenv('nexusPassword')
	];

	/**
	 * MAIN Method.
	 */
	def static main( def args ) {
                println "in main method"
		NexusArtifactCleanup cleanup = new NexusArtifactCleanup();
		cleanup.cleanupArtifacts();
	}

	/**
	 * Controller method. Has 3 steps.
	 * 1) Find Artifacts to delete.
	 * 2) Delete Artifacts.
	 * 3) Rebuild Nexus Repository Metadata.
	 */
	def cleanupArtifacts() {
                println "i am here"
		def nexusRepositoriesServiceUrl = settings.baseUrl + '/service/local/repositories/' +  settings.repositoryId + '/content/';
		def nexusMetadataServiceUrl = settings.baseUrl + '/service/local/metadata/repositories/' +  settings.repositoryId + '/content/';

		// Find all repository items that match the regular expression pattern defined in settings.
		def urls = findArtifacts(nexusRepositoriesServiceUrl, settings.pattern, settings.age);
                
		// The number of artifacts to be deleted.
		def size = urls.size();

		// Delete each artifact via the Nexus Rest API.
		urls.each {
			println "DELETING... " + it;
			//deleteContent(it, settings.nexusUsername, settings.nexusPassword);
		}

		// Rebuild Nexus repository metadata, if we have deleted artifacts.
		if (size > 0) {
			println "REBUILDING REPOSITORY METADATA... ";
			//rebuildRepoMetadata(nexusMetadataServiceUrl, settings.nexusUsername, settings.nexusPassword);
		}
	}

	/**
	 * Finds artifacts that match the inputted regex pattern, and meet the age requirement.
	 */
	def findArtifacts ( String url, String pattern, int age ) {

		def artifactUrls = [];
		def xml = fetchContent(url);
                println "test" + xml
		xml.data.'content-item'.each {
			def text = it.text.text();
			def resourceURI = it.resourceURI.text();
			def isLeaf = it.leaf.text();

			if (text ==~ pattern) {
				def lastModifiedDate = new Date().parse('yyyy-MM-dd HH:mm:ss.SSS z', it.lastModified.text());
                                 println lastModifiedDate
				if ((new Date() - age) >  lastModifiedDate) {
					artifactUrls << resourceURI;
				}
			}
			if (isLeaf == 'false') {
				artifactUrls += findArtifacts(resourceURI, pattern, age);
			}
		}

		return artifactUrls;
	}

	/**
	 * Queries Nexus Repository.
	 */
	def fetchContent ( String url ) {
		RESTClient rc = new RESTClient(url);
		def response = rc.get(contentType: XML);
		return response.data;
	}

	/**
	 * Deletes Nexus Artifact.
	 */
	def deleteContent (String url, String username, String password ) {
		RESTClient rc = new RESTClient(url);
		rc.auth.basic(username, password);
		def response = rc.delete(contentType: ANY);
	}

	/**
	 * Rebuilds Nexus Metadata.
	 */
	def rebuildRepoMetadata( String url, String username, String password  ) {
		RESTClient rc = new RESTClient(url);
		rc.auth.basic(username, password);
		def response = rc.delete(contentType: ANY);
	}
}
