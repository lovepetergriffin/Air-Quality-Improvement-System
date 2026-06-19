<template>
  <div class="wrapper">
    <header class="screen-header">
      <img src="../assets/header.png" alt="东软空气质量环保公众监督平台" />
      <div class="header-left">空气质量监测可视化大屏</div>
      <div class="header-right">
        <span class="pulse"></span>
        <span>{{ currentTime }}</span>
      </div>
    </header>

    <main class="main">
      <aside class="side left-side">
        <section class="panel grid-panel">
          <div class="gauge-item">
            <h4>全国省网格覆盖率(%)</h4>
            <GaugeChart gaugecolor="#32ccee" :gaugedata="provinceCoverage" />
          </div>
          <div class="gauge-item">
            <h4>全国大城市网格覆盖率(%)</h4>
            <GaugeChart gaugecolor="#22a06b" :gaugedata="cityCoverage" />
          </div>
        </section>

        <section class="panel">
          <PieChart :pie-data="pieArrData" />
        </section>

        <section class="panel">
          <LineChart :linenamedata="lineNameData" :linevaluedata="lineValueData" />
        </section>
      </aside>

      <section class="center">
        <section class="map-panel">
          <ChinaMap :map-arr-data="mapArrData" />
        </section>

        <section class="statistics panel">
          <h3>空气质量检测实时统计</h3>
          <ul>
            <li>
              <span class="stat-icon">总</span>
              <div>
                <h4>{{ aqiCount }}</h4>
                <p>空气质量检测总数量</p>
              </div>
            </li>
            <li>
              <span class="stat-icon good">优</span>
              <div>
                <h4>{{ aqiGoodCount }}</h4>
                <p>空气质量良好数量</p>
              </div>
            </li>
            <li>
              <span class="stat-icon warn">污</span>
              <div>
                <h4>{{ pollutionCount }}</h4>
                <p>空气质量污染数量</p>
              </div>
            </li>
          </ul>
        </section>
      </section>

      <article class="side right-side">
        <section class="panel">
          <BarChart
            bartext="悬浮颗粒物（PM2.5）浓度超标累计"
            barcolor="#f5d76e"
            :namedata="provinceNameData"
            :valuedata="spmValueData"
          />
        </section>
        <section class="panel">
          <BarChart
            bartext="二氧化硫（SO2）浓度超标累计"
            barcolor="#32ccee"
            :namedata="provinceNameData"
            :valuedata="so2ValueData"
          />
        </section>
        <section class="panel">
          <BarChart
            bartext="一氧化碳（CO）浓度超标累计"
            barcolor="#22a06b"
            :namedata="provinceNameData"
            :valuedata="coValueData"
          />
        </section>
      </article>
    </main>

    <footer class="screen-footer">
      <span>数据来源：NEP 环保公众监督系统</span>
      <span>{{ loadState }}</span>
    </footer>
  </div>
</template>

<script setup>
import axios from 'axios';
import { computed, onBeforeUnmount, onMounted, ref } from 'vue';
import BarChart from '../components/BarChart.vue';
import ChinaMap from '../components/ChinaMap.vue';
import GaugeChart from '../components/GaugeChart.vue';
import LineChart from '../components/LineChart.vue';
import PieChart from '../components/PieChart.vue';

const host = window.location.hostname || 'localhost';
const baseURL = import.meta.env.VITE_API_BASE_URL || `http://${host}:8080/`;
const api = axios.create({ baseURL, timeout: 8000 });

const currentTime = ref('');
const loadState = ref('等待数据刷新');
const provinceNameData = ref([]);
const so2ValueData = ref([]);
const coValueData = ref([]);
const spmValueData = ref([]);
const provinceCoverage = ref(0);
const cityCoverage = ref(0);
const pieArrData = ref([]);
const lineNameData = ref([]);
const lineValueData = ref([]);
const mapArrData = ref([]);
const aqiCount = ref(0);
const aqiGoodCount = ref(0);
let clockTimer = null;
let refreshTimer = null;

const pollutionCount = computed(() => Math.max(0, Number(aqiCount.value) - Number(aqiGoodCount.value)));

const demoProvinceStats = [
  { provinceName: '北京市', statCount: 8, avgSo2Value: 22, avgCoValue: 8, avgSpmValue: 45 },
  { provinceName: '山西省', statCount: 12, avgSo2Value: 42, avgCoValue: 14, avgSpmValue: 76 },
  { provinceName: '上海市', statCount: 7, avgSo2Value: 26, avgCoValue: 10, avgSpmValue: 51 },
  { provinceName: '江苏省', statCount: 10, avgSo2Value: 32, avgCoValue: 12, avgSpmValue: 68 },
  { provinceName: '辽宁省', statCount: 5, avgSo2Value: 38, avgCoValue: 16, avgSpmValue: 82 }
];

const demoDistribution = [
  { chineseExplain: '优', count: 9, color: '#02e300' },
  { chineseExplain: '良', count: 12, color: '#5fbf00' },
  { chineseExplain: '轻污', count: 8, color: '#ff7e00' },
  { chineseExplain: '中污', count: 5, color: '#fe0000' },
  { chineseExplain: '重污', count: 3, color: '#98004b' }
];

const demoTrend = ['01', '02', '03', '04', '05', '06', '07', '08'].map((date, index) => ({
  date,
  avgAqiLevel: [1.4, 1.7, 2.3, 2.0, 2.8, 3.2, 2.6, 2.1][index],
  recordCount: [4, 6, 5, 7, 9, 8, 6, 5][index]
}));

const shortName = name => String(name || '')
  .replace('省', '')
  .replace('市', '')
  .replace('自治区', '')
  .replace('壮族', '')
  .replace('回族', '')
  .replace('维吾尔', '');

const numberValue = value => {
  const num = Number(value);
  return Number.isFinite(num) ? Number(num.toFixed(1)) : 0;
};

const unwrap = response => response?.data?.data ?? response?.data ?? null;

const fetchData = async (url, fallback) => {
  try {
    const response = await api.get(url);
    const data = unwrap(response);
    return data == null ? fallback : data;
  } catch (error) {
    console.warn(`数据看板接口请求失败：${url}`, error);
    return fallback;
  }
};

const updateTime = () => {
  const now = new Date();
  const pad = value => String(value).padStart(2, '0');
  currentTime.value = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`;
};

const applyProvinceStats = stats => {
  const rows = Array.isArray(stats) && stats.length ? stats : demoProvinceStats;
  const provinceMap = new Map();
  rows.forEach(item => {
    const name = item.provinceName || item.cityName;
    if (!name) return;
    const statCount = Number(item.statCount || item.aqiTotal || 1) || 1;
    const oldValue = provinceMap.get(name) || {
      name,
      statCount: 0,
      so2Total: 0,
      coTotal: 0,
      spmTotal: 0
    };
    oldValue.statCount += statCount;
    oldValue.so2Total += numberValue(item.avgSo2Value ?? item.so2Total) * statCount;
    oldValue.coTotal += numberValue(item.avgCoValue ?? item.coTotal) * statCount;
    oldValue.spmTotal += numberValue(item.avgSpmValue ?? item.spmTotal) * statCount;
    provinceMap.set(name, oldValue);
  });
  const provinceRows = Array.from(provinceMap.values())
    .sort((a, b) => b.statCount - a.statCount || a.name.localeCompare(b.name, 'zh-CN'));

  provinceNameData.value = provinceRows.map(item => shortName(item.name));
  so2ValueData.value = provinceRows.map(item => numberValue(item.so2Total / item.statCount));
  coValueData.value = provinceRows.map(item => numberValue(item.coTotal / item.statCount));
  spmValueData.value = provinceRows.map(item => numberValue(item.spmTotal / item.statCount));
  mapArrData.value = provinceRows.map(item => ({ name: item.name, value: item.statCount }));
};

const applyDistribution = data => {
  const rows = Array.isArray(data) && data.length ? data : demoDistribution;
  pieArrData.value = rows.map(item => ({
    name: item.chineseExplain || item.aqiExplain || `AQI ${item.aqiId}`,
    value: Number(item.count ?? item.total ?? 0),
    itemStyle: { color: item.color || undefined }
  }));
};

const applyTrend = data => {
  const rows = Array.isArray(data) && data.length ? data : demoTrend;
  lineNameData.value = rows.map(item => item.date || item.month || item.name);
  lineValueData.value = rows.map(item => numberValue(item.avgAqiLevel ?? item.recordCount ?? item.total));
};

const init = async () => {
  const [provinceStats, cityRate, provinceRate, distribution, trend, total, good] = await Promise.all([
    fetchData('statistics/listProvinceItemTotalStatis', demoProvinceStats),
    fetchData('statistics/getCityCoverage', 0),
    fetchData('statistics/getProvinceCoverage', 0),
    fetchData('statistics/listAqiDistributeTotalStatis', demoDistribution),
    fetchData('statistics/listAqiTrendTotalStatis', demoTrend),
    fetchData('statistics/getAqiCount', 0),
    fetchData('statistics/getAqiGoodCount', 0)
  ]);

  applyProvinceStats(provinceStats);
  cityCoverage.value = numberValue(cityRate);
  provinceCoverage.value = numberValue(provinceRate);
  applyDistribution(distribution);
  applyTrend(trend);
  aqiCount.value = Number(total || 0);
  aqiGoodCount.value = Number(good || 0);
  loadState.value = `最近刷新：${currentTime.value || '刚刚'}`;
};

onMounted(() => {
  updateTime();
  init();
  clockTimer = setInterval(updateTime, 1000);
  refreshTimer = setInterval(init, 5000);
});

onBeforeUnmount(() => {
  clearInterval(clockTimer);
  clearInterval(refreshTimer);
});
</script>

<style scoped>
.wrapper {
  width: 100%;
  height: 100%;
  min-width: 1180px;
  min-height: 680px;
  display: flex;
  flex-direction: column;
  color: #e7fbff;
  background:
    radial-gradient(circle at 50% 35%, rgba(20, 111, 167, 0.36), transparent 38%),
    linear-gradient(180deg, #001432 0%, #001b3f 45%, #000f28 100%);
}

.screen-header {
  position: relative;
  height: 62px;
  flex: 0 0 62px;
}

.screen-header img {
  width: 100%;
  height: 100%;
  display: block;
  object-fit: cover;
}

.header-left,
.header-right {
  position: absolute;
  top: 21px;
  font-size: 14px;
  color: #aaf9fc;
}

.header-left {
  left: 28px;
}

.header-right {
  right: 28px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.pulse {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #22a06b;
  box-shadow: 0 0 12px #22a06b;
}

.main {
  flex: 1;
  display: flex;
  gap: 14px;
  padding: 12px 18px 8px;
  overflow: hidden;
}

.side {
  flex: 0 0 28%;
  display: flex;
  flex-direction: column;
  gap: 14px;
  min-width: 0;
}

.center {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 14px;
  min-width: 0;
}

.panel,
.map-panel {
  position: relative;
  border: 1px solid rgba(78, 199, 255, 0.34);
  background: linear-gradient(180deg, rgba(7, 34, 72, 0.78), rgba(4, 24, 57, 0.82));
  box-shadow: inset 0 0 22px rgba(50, 204, 238, 0.08), 0 0 18px rgba(0, 0, 0, 0.14);
  overflow: hidden;
}

.panel::before,
.map-panel::before {
  content: "";
  position: absolute;
  inset: 0;
  border-top: 2px solid rgba(50, 204, 238, 0.42);
  pointer-events: none;
}

.grid-panel {
  height: 28%;
  display: flex;
}

.gauge-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.gauge-item h4 {
  height: 34px;
  margin: 12px 0 0;
  text-align: center;
  font-size: 14px;
  font-weight: 500;
  color: #d8fbff;
}

.left-side .panel:nth-child(2),
.left-side .panel:nth-child(3),
.right-side .panel {
  flex: 1;
}

.map-panel {
  flex: 1;
}

.statistics {
  height: 150px;
  flex: 0 0 150px;
}

.statistics h3 {
  margin: 18px 0 14px;
  text-align: center;
  font-size: 16px;
  font-weight: 500;
  color: #d8fbff;
}

.statistics ul {
  display: flex;
  justify-content: space-around;
  color: #ffffff;
}

.statistics li {
  display: flex;
  align-items: center;
  min-width: 170px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  margin-right: 12px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  color: #001432;
  font-size: 22px;
  font-weight: 700;
  background: linear-gradient(135deg, #32ccee, #97f1ff);
}

.stat-icon.good {
  background: linear-gradient(135deg, #22a06b, #7ce0b8);
}

.stat-icon.warn {
  background: linear-gradient(135deg, #fa8c16, #f5d76e);
}

.statistics h4 {
  margin: 0;
  font-size: 30px;
  line-height: 1.1;
  color: #ffffff;
}

.statistics p {
  margin: 5px 0 0;
  font-size: 13px;
  color: #9fcbe6;
}

.screen-footer {
  height: 30px;
  flex: 0 0 30px;
  padding: 0 28px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #70b8d8;
  font-size: 12px;
  border-top: 1px solid rgba(78, 199, 255, 0.2);
}

@media (max-width: 1280px) {
  .wrapper {
    min-width: 1000px;
  }

  .statistics li {
    min-width: 145px;
  }
}
</style>
