<template>
  <div ref="chartBox" class="chart"></div>
</template>

<script setup>
import * as echarts from 'echarts';
import { onBeforeUnmount, onMounted, ref, watch } from 'vue';

const props = defineProps({
  linenamedata: { type: Array, default: () => [] },
  linevaluedata: { type: Array, default: () => [] }
});

const chartBox = ref(null);
let chart = null;

const renderChart = () => {
  if (!chart) return;
  chart.setOption({
    backgroundColor: 'transparent',
    title: {
      text: '近 12 期 AQI 趋势',
      left: 'center',
      top: 8,
      textStyle: { color: '#d8fbff', fontSize: 14, fontWeight: 500 }
    },
    tooltip: { trigger: 'axis' },
    grid: { left: 42, right: 20, top: 54, bottom: 34 },
    xAxis: {
      type: 'category',
      data: props.linenamedata,
      boundaryGap: false,
      axisLabel: { color: '#9fcbe6', fontSize: 11 },
      axisLine: { lineStyle: { color: '#1e6b8f' } }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: 'rgba(61, 164, 207, 0.18)' } },
      axisLabel: { color: '#9fcbe6' }
    },
    series: [
      {
        type: 'line',
        smooth: true,
        data: props.linevaluedata,
        symbolSize: 7,
        lineStyle: { width: 3, color: '#32ccee' },
        itemStyle: { color: '#f5d76e' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(50, 204, 238, 0.38)' },
            { offset: 1, color: 'rgba(50, 204, 238, 0.02)' }
          ])
        }
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

watch(() => [props.linenamedata, props.linevaluedata], renderChart, { deep: true });
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
