export function formatDuration(duration: number): string {
  const hours = Math.floor(duration / 60);
  const minutes = duration % 60;
  const h = hours < 1 ? '' : `${hours.toString().padStart(2, '0')}h`;
  const m = minutes < 1 ? '' : `${minutes.toString().padStart(2, '0')}m`;
  return `${h}${m}`;
}
