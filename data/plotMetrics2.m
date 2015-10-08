function plotMetrics(data, postfix)
    function plotHist(func1, func2, transform, heading, fileBase, figureNum)
        if ~ishandle(figureNum)
            figure(figureNum)
        end
        set(0, 'CurrentFigure', figureNum);
        clf;
        tmp1 = arrayfun(@(x) transform(func1(x)), s);
        tmp2 = arrayfun(@(x) transform(func2(x)), s);
        dataMin = min([tmp1 tmp2]);
        dataMax = max([tmp1 tmp2]);
        edges = linspace(dataMin,dataMax,100);
        cnts1 = histc(tmp1, edges);
        cnts2 = histc(tmp2, edges);
        bar(edges,cnts1,'FaceColor', 'r');
        hold on;
        bar(edges,cnts2,'FaceColor', 'b');

        xlabel(heading);
        ylabel('Counts');
        title(sprintf('%s\n(features=%d, emptiness=%.0d%%, %dx%d, %s, %d runs)',heading,numFeatures,emptiness*100,cols,rows,strat, length(tmp1)));
        legend('Initial','Final');
        drawnow;
        print(sprintf('%s%s.png',fileBase,postfix),'-dpng');
    end
    if isstr(data)
        s = readTSV(data);
    else
        s = data;
    end

    numFeatures = s(1).numFeatures;
    emptiness = s(1).emptiness;
    cols = s(1).cols;
    rows = s(1).rows;
    strat = s(1).strat;

    if nargin < 2
        postfix = sprintf('_%d_%.0d_%dx%d_%s',numFeatures, emptiness*100, cols, rows, strat(1));
    end
    
    plotHist(@(x)(x.beforeSimilarity), @(x)(x.afterSimilarity), @mean, 'Average Agent Similarity Score', 'Similarity', 1);
    plotHist(@(x)(x.beforeMooreClustering), @(x)(x.afterMooreClustering), @mean, 'Average Moore Cluster Size', 'Moore', 2);
    plotHist(@(x)(x.beforeMooreClustering), @(x)(x.afterMooreClustering), @length, 'Number of Moore Clusters', 'MooreCnt', 3);
    plotHist(@(x)(x.beforeNeumannClustering), @(x)(x.afterNeumannClustering), @mean, 'Average von Neumann Cluster Size', 'Neumann', 4);
    plotHist(@(x)(x.beforeNeumannClustering), @(x)(x.afterNeumannClustering), @length, 'Number of von Neumann Clusters', 'NeumannCnt', 5);
end
