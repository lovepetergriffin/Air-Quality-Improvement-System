<template>
  <div ref="chartBox" class="chart"></div>
</template>

<script setup>
import * as echarts from 'echarts';
import { onBeforeUnmount, onMounted, ref, watch } from 'vue';

const props = defineProps({
  pieData: { type: Array, default: () => [] }
});

const chartBox = ref(null);
let chart = null;

const renderChart = () => {
  if (!chart) return;
  chart.setOption({
    backgroundColor: 'transparent',
    title: {
      text: 'AQI 等级分布',
      left: 'center',
      top: 8,
      textStyle: { color: '#d8fbff', fontSize: 14, fontWeight: 500 }
    },
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: {
      bottom: 0,
      icon: 'circle',
      textStyle: { color: '#9fcbe6', fontSize: 11 }
    },
    series: [
      {
        type: 'pie',
        radius: ['42%', '68%'],
        center: ['50%', '50%'],
        data: props.pieData,
        label: { color: '#e7fbff', formatter: '{b}\n{c}' },
        labelLine: { lineStyle: { color: '#69d7ff' } },
        itemStyle: { borderColor: '#001432', borderWidth: 2 }
      }
    ]
  });
};

const resizeChart = () => chart && chart.resize();

onMounted(() => {
  chart = echarts.init(chartBox.value);
  renderChart();
  window.addEventListener('resize', resizeChart);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart);
  chart && chart.dispose();
});

watch(() => props.pieData, renderChart, { deep: true });
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
