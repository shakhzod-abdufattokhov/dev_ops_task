# 📧 EmailSender  
**DevOps & Observability Showcase**

Modern GitOps pipeline + full observability stack for a Spring Boot application — running locally on **Docker Desktop**.

![GitOps + Observability](https://img.shields.io/badge/GitOps-ArgoCD-blue?style=for-the-badge&logo=argocd)  
![Observability](https://img.shields.io/badge/Metrics-Prometheus-orange?style=for-the-badge&logo=prometheus) ![Logs](https://img.shields.io/badge/Logs-Loki-black?style=for-the-badge&logo=grafana) ![Visualization](https://img.shields.io/badge/Dashboards-Grafana-ff69b4?style=for-the-badge&logo=grafana)

---

## 🎯 Project Goal

Demonstrate production-grade DevOps practices in a local environment:

- **GitOps** workflow with ArgoCD  
- **Helm** packaging (manually written chart)  
- Metrics collection & visualization (**Prometheus** + **Grafana**)  
- Centralized structured logging (**Loki** + **Promtail**)  
- Persistent storage with **H2** (file-based)  
- Debugging real-world issues on resource-constrained local clusters

---

## 🏗️ Architecture
GitHub (source of truth)
│
▼
ArgoCD Application
│
▼
Helm Chart (emailsender)
│
▼
Kubernetes (Docker Desktop)
│
▼
Spring Boot App ──► EmailSender
│
└─► Observability
├─► Prometheus  (metrics scraping)
├─► Grafana     (dashboards & exploration)
└─► Loki        (log aggregation + Promtail)
text---

## 🛠️ Tech Stack

| Category           | Tools & Versions                              |
|--------------------|-----------------------------------------------|
| Language           | Java 17                                       |
| Framework          | Spring Boot                                   |
| Container          | Docker                                        |
| Orchestration      | Kubernetes (Docker Desktop)                   |
| Packaging          | Helm 3 (manual chart)                         |
| GitOps             | ArgoCD                                        |
| Monitoring         | kube-prometheus-stack                         |
| Visualization      | Grafana                                       |
| Logging            | loki-stack (Loki + Promtail)                  |
| Database           | H2 (file-based)                               |
| CI                 | GitHub Actions                                |

---

## 📁 Folder Structure

```text
EmailSender/
├── src/                        # Spring Boot application
├── Dockerfile
├── pom.xml
│
├── helm/
│   └── emailsender/
│       ├── Chart.yaml
│       ├── values.yaml
│       └── templates/
│           ├── deployment.yaml
│           ├── service.yaml
│           ├── pvc.yaml
│           ├── servicemonitor.yaml
│           └── configmap.yaml
│
├── argocd/
│   └── application.yaml        # ArgoCD Application CR
│
└── README.md

🚀 Quick Start
1. Build & Push Docker Image
Bashdocker build -t shakhzodjon/emailsender:latest .
docker push shakhzodjon/emailsender:latest
2. Install Monitoring Stack
Bashhelm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

kubectl create namespace monitoring

helm install monitoring prometheus-community/kube-prometheus-stack \
  --namespace monitoring
3. Install Loki Stack (required name)
Bashhelm repo add grafana https://grafana.github.io/helm-charts
helm repo update

helm install loki-stack grafana/loki-stack --namespace monitoring
4. Deploy via GitOps (ArgoCD)
Bash# Apply ArgoCD Application manifest
kubectl apply -f argocd/application.yaml
Access ArgoCD UI:
Bashkubectl port-forward svc/argocd-server -n argocd 8081:443
→ Open https://localhost:8081
Username: admin
Password:
Bashkubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 --decode

📊 Observability Access
Grafana
Bashkubectl port-forward svc/monitoring-grafana -n monitoring 3000:80
→ http://localhost:3000
(default: admin / prom-operator)
Loki (test readiness)
Bashkubectl port-forward svc/loki-stack -n monitoring 3100:3100
curl http://localhost:3100/ready
Add Loki datasource in Grafana:

URL: http://loki-stack.monitoring:3100
Access: Server


🔍 Example Queries
PromQL (Metrics)
promql# App availability
up{app_kubernetes_io_name="emailsender"}

# Requests / sec
sum(rate(http_server_requests_seconds_count[1m]))

# 5xx errors
sum(rate(http_server_requests_seconds_count{status=~"5.."}[5m]))

# P95 latency
histogram_quantile(0.95, sum by (le) (rate(http_server_requests_seconds_bucket[5m])))
LogQL (Logs)
logql# All app logs
{namespace="default", app="emailsender"}

# Errors only
{namespace="default", app="emailsender"} |= "ERROR"

[!IMPORTANT]
H2 Database Constraints
Must run with replicaCount: 1
/data volume mounted via PVC
Not suitable for production — demo only

✅ Status – All Systems Green

✔ ArgoCD GitOps sync working
✔ Prometheus scraping /actuator/prometheus
✔ Grafana dashboards & alerts
✔ Loki + Promtail log collection
✔ Persistent H2 storage
✔ Manual Helm chart
✔ Instructor requirements met


👤 Author
Shakhzod Abdufattokhov
DevOps / Java Backend Student
Tashkent · January 2026
