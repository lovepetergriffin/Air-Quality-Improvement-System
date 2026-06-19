<template>
  <div ref="chartBox" class="chart"></div>
</template>

<script setup>
import * as echarts from 'echarts';
import '../assets/china.js';
import { onBeforeUnmount, onMounted, ref, watch } from 'vue';

const props = defineProps({
  mapArrData: { type: Array, default: () => [] }
});

const chartBox = ref(null);
let chart = null;

const renderChart = () => {
  if (!chart) return;
  const maxValue = Math.max(5, ...props.mapArrData.map(item => Number(item.value || 0)));
  chart.setOption({
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'item',
      formatter: params => `${params.name}<br/>检测数量：${params.value || 0}`
    },
    visualMap: {
      min: 0,
      max: maxValue,
      left: 24,
      bottom: 20,
      text: ['高', '低'],
      textStyle: { color: '#9fcbe6' },
      calculable: true,
      inRange: { color: ['#0c2f5d', '#1e96c7', '#32ccee', '#f5d76e', '#ff7f50'] }
    },
    series: [
      {
        name: '省份检测数据',
        type: 'map',
        map: 'china',
        roam: true,
        zoom: 1.18,
        label: { color: '#d8fbff' },
        emphasis: {
          label: { color: '#ffffff' },
          itemStyle: { areaColor: '#22a06b' }
        },
        itemStyle: {
          areaColor: '#0c2f5d',
          borderColor: '#5ed4ff',
          borderWidth: 1
        },
        data: props.mapArrData
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

watch(() => props.mapArrData, renderChart, { deep: true });
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
