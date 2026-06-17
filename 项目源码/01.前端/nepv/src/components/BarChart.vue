<template>
  <div ref="chartBox" class="chart"></div>
</template>

<script setup>
import * as echarts from 'echarts';
import { onBeforeUnmount, onMounted, ref, watch } from 'vue';

const props = defineProps({
  bartext: { type: String, required: true },
  barcolor: { type: String, required: true },
  namedata: { type: Array, default: () => [] },
  valuedata: { type: Array, default: () => [] }
});

const chartBox = ref(null);
let chart = null;

const renderChart = () => {
  if (!chart) return;
  chart.setOption({
    backgroundColor: 'transparent',
    title: {
      text: props.bartext,
      left: 'center',
      top: 8,
      textStyle: { color: '#d8fbff', fontSize: 14, fontWeight: 500 }
    },
    grid: { left: 44, right: 18, top: 54, bottom: 34 },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    xAxis: {
      type: 'category',
      data: props.namedata,
      axisLabel: { color: '#9fcbe6', interval: 0, fontSize: 11 },
      axisLine: { lineStyle: { color: '#1e6b8f' } },
      axisTick: { show: false }
    },
    yAxis: {
      type: 'value',
      splitLine: { lineStyle: { color: 'rgba(61, 164, 207, 0.18)' } },
      axisLabel: { color: '#9fcbe6' }
    },
    series: [
      {
        type: 'bar',
        data: props.valuedata,
        barWidth: 14,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: props.barcolor },
            { offset: 1, color: '#0b6e99' }
          ]),
          borderRadius: [4, 4, 0, 0]
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

watch(() => [props.namedata, props.valuedata, props.bartext, props.barcolor], renderChart, { deep: true });
</script>

<style scoped>
.chart {
  width: 100%;
  height: 100%;
}
</style>
