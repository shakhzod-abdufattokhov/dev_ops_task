{{/*
Expand the name of the chart.
*/}}
{{- define "emailsender.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
*/}}
{{- define "emailsender.fullname" -}}
{{- if .Values.nameOverride }}
{{- .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "emailsender.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "emailsender.labels" -}}
helm.sh/chart: {{ include "emailsender.chart" . }}
{{ include "emailsender.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "emailsender.selectorLabels" -}}
app.kubernetes.io/name: {{ include "emailsender.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Prometheus labels
*/}}
{{- define "emailsender.prometheusLabels" -}}
helm.sh/chart: {{ include "emailsender.chart" . }}
app.kubernetes.io/name: {{ include "emailsender.name" . }}-prometheus
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Values.prometheus.image.tag | quote }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Prometheus selector labels
*/}}
{{- define "emailsender.prometheusSelectorLabels" -}}
app.kubernetes.io/name: {{ include "emailsender.name" . }}-prometheus
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}
