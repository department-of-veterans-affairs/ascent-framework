@Library('ascent@development') _

mavenPipeline {
    triggers = [
        upstream(
        threshold: 'SUCCESS',
        upstreamProjects: '../ascent-libraries-parent/development'
        )
    ]
}