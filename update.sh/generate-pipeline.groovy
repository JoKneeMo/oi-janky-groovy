properties([
	buildDiscarder(logRotator(numToKeepStr: '10')),
	disableConcurrentBuilds(),
	pipelineTriggers([
		cron('H H * * H'),
	]),
])

node('master') {
	stage('Checkout') {
		checkout([
			$class: 'GitSCM',
			userRemoteConfigs: [
				[url: 'https://github.com/JoKneeMo/oi-janky-groovy.git'],
			],
			branches: [
				[name: '*/master'],
			],
			extensions: [
				[
					$class: 'CleanCheckout',
				],
				[
					$class: 'RelativeTargetDirectory',
					relativeTargetDir: 'oi-janky-groovy',
				],
			],
			doGenerateSubmoduleConfigurations: false,
			submoduleCfg: [],
		])
	}

	stage('Generate') {
		jobDsl(
			lookupStrategy: 'SEED_JOB',
			removedJobAction: 'DELETE',
			removedViewAction: 'DELETE',
			targets: 'oi-janky-groovy/update.sh/dsl.groovy',
		)
	}
}
